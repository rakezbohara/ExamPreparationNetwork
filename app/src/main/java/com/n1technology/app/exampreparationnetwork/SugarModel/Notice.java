package com.n1technology.app.exampreparationnetwork.SugarModel;

import com.orm.SugarRecord;

/**
 * Created by RAKEZ on 12/28/2017.
 */

public class Notice extends SugarRecord<Notice> {
    private String noticeTitle;
    private String noticeBody;
    private String noticeDate;

    public Notice() {
    }

    public Notice(String noticeTitle, String noticeBody, String noticeDate) {
        this.noticeTitle = noticeTitle;
        this.noticeBody = noticeBody;
        this.noticeDate = noticeDate;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeBody() {
        return noticeBody;
    }

    public void setNoticeBody(String noticeBody) {
        this.noticeBody = noticeBody;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }
}
