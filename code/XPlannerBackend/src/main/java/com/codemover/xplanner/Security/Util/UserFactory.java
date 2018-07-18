package com.codemover.xplanner.Security.Util;

import com.codemover.xplanner.Model.Entity.JAccountUser;
import com.codemover.xplanner.Security.Exception.ParseProfileJsonException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;

public class UserFactory {

    public static JAccountUser createJAccountUser(String jsonOfProfile)
            throws ParseException, ParseProfileJsonException {
        //Parse JSON

        JSONObject json = new JSONObject(jsonOfProfile);
        int errno = json.getInt("errno");
        if (errno != 0)
            throw new ParseProfileJsonException(json.getString("error"));
        JSONArray entities = json.getJSONArray("entities");
        JSONObject entity = entities.getJSONObject(0);
        String uniqueId = entity.getString("id");
        String jAccountName = entity.getString("account");
        String realName = entity.getString("name");
        String studentID = entity.getString("code");
        String classNumber = entity.getString("classNo");
        JSONArray identities = entity.getJSONArray("identities");
        JSONObject identity = identities.getJSONObject(0);

        //Create the JAccount User

        JAccountUser jAccountUser = new JAccountUser();
        //TODO ADD USER
        jAccountUser.setUniqueId(uniqueId);
        jAccountUser.setjAccountName(jAccountName);
        jAccountUser.setStudentId(studentID);
        jAccountUser.setRealName(realName);
        jAccountUser.setClassNumber(classNumber);
        jAccountUser.setUserName(jAccountName);

        return jAccountUser;
    }
}
