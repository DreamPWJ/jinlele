package com.jinlele.util.weixinUtils.menu;



/**
 * 复合类型的按钮
 * 就是代表任意的一级菜单以及子菜单
 */
public class ComplexButton extends Button {

    private Button[] sub_button;

    public Button[] getSub_button() {
        return sub_button;
    }

    public void setSub_button(Button[] sub_button) {
        this.sub_button = sub_button;
    }
}
