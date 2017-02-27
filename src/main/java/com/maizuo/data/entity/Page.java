package com.maizuo.data.entity;

/**
 * @author rose
 * @ClassName: Page
 * @Email rose@maizuo.com
 * @create 2017/1/10-16:54
 * @Description: 分页实体类
 */
public class Page {

    public Page(int total,int currrent){
        this.total = total;
        this.current = currrent;
    }

   //当前页
    private int current;
    private int total;

    //总数量
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
