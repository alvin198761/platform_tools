package org.alvin;

import com.google.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 小蓝充电费率时间范围处理类
 * @author 唐植超
 * @date 2020/05/18
 */
public class XblueDataRange {

    private Date start;
    private Date end;

    public boolean contains(Date date) {
        return start.getTime() <= date.getTime() && end.getTime() > date.getTime();
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(this.start) +"~"+format.format(this.end);
    }

    /**
     * 小蓝时间段计算
     * @return
     */
    public static List<XblueDataRange> getXblueTimeRange(){
        List<XblueDataRange> ranges = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //计算出所有的时间段
        for (int i = 0; i < 48; i++) {
            XblueDataRange range = new XblueDataRange();
            range.setStart(calendar.getTime());
            calendar.add(Calendar.MINUTE, 30);
            range.setEnd(calendar.getTime());
            ranges.add(range);
            System.out.println(range);
        }
        return ranges;
    }
}
