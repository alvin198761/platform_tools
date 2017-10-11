package org.alvin.api.utils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ServiceObjectFactory {

	private static Map<Class<?>, Object> instanceMap = new HashMap<Class<?>, Object>();

	private static Map<String, List<Class<?>>> serviceMap = new HashMap<String, List<Class<?>>>();

	/**
	 * 业务实例对象创建和获取
	 * 
	 * @param cl
	 * @return
	 */
	private static <T> T craeteInstance(Class<? extends T> cl) {
		if (!instanceMap.containsKey(cl)) {
			try {
				instanceMap.put(cl, cl.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (T) instanceMap.get(cl);
	}

	/**
	 * 扫描业务类
	 * 
	 * @param packageURL
	 * @throws Exception
	 */
	public static void doScanService(String packageURL) throws Exception {
		List<Class> list = new ArrayList<Class>();
		try {
			String packageName = packageURL;
			packageURL = packageURL.replaceAll("[.]", "/");
			URL baseURL = Thread.currentThread().getContextClassLoader().getResource(packageURL);
			if ("file".equals(baseURL.getProtocol())) {
				list.addAll(doDevScan(baseURL, packageName));
			} else if ("jar".equals(baseURL.getProtocol())) {
				list.addAll(doRuntimeScan(baseURL, packageName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		initService(list);
	}

	/**
	 * 将扫描的类进行过滤，不是该注解的一律放弃掉
	 * 
	 * @param list
	 * @throws Exception
	 */
	private static void initService(List<Class> list) throws Exception {
		loop: for (Class c : list) {
			// 获取类的所有注解
			Annotation[] ac = c.getDeclaredAnnotations();
			for (final Annotation a : ac) {
				// 过滤所有的业务类注解
				if (!a.annotationType().equals(ClusterService.class)) {
					continue;
				}
				// 获取注解的值
				String value = AccessController.doPrivileged(new PrivilegedAction<String>() {

					@Override
					public String run() {
						Method m = null;
						try {
							m = a.getClass().getMethod("value");
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (m == null) {
							return null;
						}
						boolean flag = m.isAccessible();
						m.setAccessible(true);
						try {
							return (String) m.invoke(a);
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
							m.setAccessible(flag);
						}
						return null;
					}

				});
				if (value == null) {
					continue;
				}
				// 根据不同的值，放入不同的业务组
				List<Class<?>> serviceList = serviceMap.get(value);
				if (serviceList == null) {
					serviceList = new ArrayList<Class<?>>();
					serviceMap.put(value, serviceList);
				}
				serviceList.add(c);
				continue loop;
			}

		}
	}

	/**
	 * 运行时只会通过这个方法调用
	 * 
	 * @param baseURL
	 * @param packageName
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static List<Class> doRuntimeScan(URL baseURL, String packageName)
			throws IOException, ClassNotFoundException {
		List<Class> classList = new ArrayList<Class>();
		//
		JarFile jar = ((JarURLConnection) baseURL.openConnection()).getJarFile();
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry je = entries.nextElement();
			String urlName = je.getName().replace('/', '.');
			if (!urlName.startsWith(packageName)) {
				continue;
			}
			if (!urlName.endsWith(".class")) {
				continue;
			}
			String classUrl = urlName.substring(0, urlName.length() - 6);
			classList.add(Class.forName(classUrl));
		}
		return classList;
	}

	/**
	 * 开发时会用到的方法
	 * 
	 * @param baseURL
	 * @param packageName
	 * @throws Exception
	 */
	private static List<Class> doDevScan(URL baseURL, String packageName) throws Exception {
		String filePath = URLDecoder.decode(baseURL.getFile(), "UTF-8");
		List<Class> classList = new ArrayList<Class>();

		LinkedList<File> files = new LinkedList<File>();
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			throw new Exception("没有找到对应的包");
		}
		files.add(dir);
		// 循环读取目录以及子目录下的所有类文件
		while (!files.isEmpty()) {
			File f = files.removeFirst();
			if (f.isDirectory()) {
				File[] fs = f.listFiles();
				int i = 0;
				for (File childFile : fs) {
					files.add(i++, childFile);
				}
				continue;
			}
			String subPath = f.getAbsolutePath().substring(dir.getAbsolutePath().length());
			String classUrl = packageName + subPath.replace(File.separatorChar, '.');
			classUrl = classUrl.substring(0, classUrl.length() - 6);
			classList.add(Class.forName(classUrl));
		}
		return classList;
	}

	/**
	 * 对外开放的获取和创建实例的方法
	 * 
	 * @param plaform
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public static <T> T getInstance(String plaform, Class<? extends T> cls) throws Exception {
		List<Class<?>> list = serviceMap.get(plaform);
		for (Class c : list) {
			for (Class i : c.getInterfaces()) {
				if ((i).equals(cls)) {
					return (T) craeteInstance(c);
				}
			}
		}
		throw new Exception("平台：" + plaform + "  " + cls.getName() + " 没有实现类");
	}



}
