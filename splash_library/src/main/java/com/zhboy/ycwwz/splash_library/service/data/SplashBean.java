package com.zhboy.ycwwz.splash_library.service.data;

import java.util.List;

public class SplashBean {

    /**
     * state : 1
     * message : success
     * data : [{"id":1,"pageImage":"http://calendar.yhzm.cc/qr/open00.jpg","type":0,"isUsing":1,"createTime":"1969-12-31T16:00:00.000+0000","description":"太特么多"},{"id":5,"pageImage":"http://calendar.yhzm.cc/qr/open01.jpg","type":0,"isUsing":1,"createTime":"1969-12-31T16:00:00.000+0000","description":"好的"},{"id":6,"pageImage":"http://calendar.yhzm.cc/qr/open02.jpg","type":0,"isUsing":1,"createTime":"1969-12-31T16:00:00.000+0000","description":"我马上做完"},{"id":7,"pageImage":"http://calendar.yhzm.cc/qr/open03.jpg","type":0,"isUsing":1,"createTime":"1969-12-31T16:00:00.000+0000","description":"真尼玛多"}]
     */

    private int state;
    private String message;
    private List<DataBean> data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * pageImage : http://calendar.yhzm.cc/qr/open00.jpg
         * type : 0
         * isUsing : 1
         * createTime : 1969-12-31T16:00:00.000+0000
         * description : 太特么多
         */

        private int id;
        private String pageImage;
        private int type;
        private int isUsing;
        private String createTime;
        private String description;
        private String jumpUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPageImage() {
            return pageImage;
        }

        public void setPageImage(String pageImage) {
            this.pageImage = pageImage;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIsUsing() {
            return isUsing;
        }

        public void setIsUsing(int isUsing) {
            this.isUsing = isUsing;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getJumpUrl() {
            return jumpUrl;
        }

        public void setJumpUrl(String jumpUrl) {
            this.jumpUrl = jumpUrl;
        }
    }
}
