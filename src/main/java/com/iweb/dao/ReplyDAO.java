package com.iweb.dao;

import com.iweb.pojo.Reply;

import java.util.List;

/**
 * @author jxy
 * @date
 */
public interface ReplyDAO {
    List<Reply> list(String message);
}
