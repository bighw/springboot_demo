package com.maizuo.kit.goods.response;

/**
 * @author rose
 * @ClassName: goods
 * @Email rose@maizuo.com
 * @create 2017/1/16-12:20
 * @Description: 模板
 */
public class GoodsInfoResponse {
    /**
     * @author rose
     * @ClassName: goodsInfo
     * @Email rose@maizuo.com
     * @create 2017/1/16-12:16
     * @Description: 商品基本信息类
     */


        private int id;
        private String name;
        private String icon;
        private String desc;
        private int price;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
}

