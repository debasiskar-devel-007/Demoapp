package com.demoapp;

import android.graphics.Bitmap;

/**
 * Created by KTA-PC 21 on 7/15/2015.
 */
public class ContactItem {
    private String email;
    private String title;

    public ContactItem(String email, String title) {
        super();
        this.email = email;
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setImage(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
