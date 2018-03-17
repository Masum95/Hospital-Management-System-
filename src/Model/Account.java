/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.scene.image.Image;

/**
 *
 * @author User
 */
public class Account implements DataModel {

    private String userID, userName, email, password, imgUrl,prevPassword;
    Image img;

    public Account() {
    }

    public Account userID(String userid) {
        this.userID = userid;
        return this;
    }

    public Account userName(String uname) {
        this.userName = uname;
        return this;
    }

    public Account email(String email) {
        this.email = email;
        return this;
    }

    public Account password(String pass) {
        this.password = pass;
        return this;
    }
    public Account prevPassword(String pass) {
        this.prevPassword = pass;
        return this;
    }

    public String getPrevPassword() {
        return prevPassword;
    }

    public Image getImg() {
        return img;
    }
    
    public Account imgUrl(String url) {
        this.imgUrl = url;
        return this;
    }
    public Account image(Image img){
        this.img = img;
        return  this;
    }
    
    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public Image getImage() {
        return img;
    }

}
