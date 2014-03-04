package com.ice.personnel.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.ice.personnel.bean.User;
import com.ice.personnel.service.IUserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    /**
     * 添加用户(该方法用来测试ajax正常调用)
     * 
     * @return
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    // @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addUser(HttpServletRequest request,
            HttpServletResponse response) {
        User user = new User();
        user.setUsername("ice");
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            userService.saveOrUpdateUser(user);
            map.put("flag", true);
        } catch (Exception e) {
            map.put("flag", false);
        }
        return map;
    }

    /**
     * 获取用户列表()
     * 
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.POST)
    // @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getUserList(HttpServletRequest request,
            HttpServletResponse response) {
        String sPage = WebUtils.findParameterValue(request, "page");
        String sPageSize = request.getParameter("pageSize");
        int page = Integer.parseInt(sPage);
        int pageSize = Integer.parseInt(sPageSize);
        Map<String, Object> map = new HashMap<String, Object>();
        List<User> userList = userService.getUserList(page, pageSize);
        map.put("userList", userList);
        return map;
    }

    /**
     * 获取用户列表
     * 
     * @return
     */
    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    // @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUserList(HttpServletRequest request) {
        String sPage = WebUtils.findParameterValue(request, "page");
        String sPageSize = request.getParameter("pageSize");
        int page = Integer.parseInt(sPage);
        int pageSize = Integer.parseInt(sPageSize);
        List<User> userList = userService.getUserList(page, pageSize);
        return userList;
    }

    /**
     * 添加用户
     * 
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    // @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public List<User> addUser(@RequestBody User user, HttpServletRequest request) {
        String sPage = WebUtils.findParameterValue(request, "page");
        String sPageSize = request.getParameter("pageSize");
        int page = Integer.parseInt(sPage);
        int pageSize = Integer.parseInt(sPageSize);
        userService.saveOrUpdateUser(user);
        List<User> userList = userService.getUserList(page, pageSize);
        return userList;
    }

    /**
     * 更新用户
     * 
     * @param user
     *            前台获取用户对象
     * @param request
     *            请求对象
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    // @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public List<User> updateUser(@RequestBody User user,
            HttpServletRequest request) {
        String sPage = WebUtils.findParameterValue(request, "page");
        String sPageSize = request.getParameter("pageSize");
        int page = Integer.parseInt(sPage);
        int pageSize = Integer.parseInt(sPageSize);
        userService.saveOrUpdateUser(user);
        List<User> list = userService.getUserList(page, pageSize);
        return list;
    }

    /**
     * 删除用户
     * 
     * @param request
     *            请求对象
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    // @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseBody
    public List<User> deleteUser(HttpServletRequest request) {
        String userId = request.getParameter("id");
        String sPage = WebUtils.findParameterValue(request, "page");
        String sPageSize = request.getParameter("pageSize");
        int page = Integer.parseInt(sPage);
        int pageSize = Integer.parseInt(sPageSize);
        User user = new User(userId);
        userService.deleteUser(user);
        List<User> list = userService.getUserList(page, pageSize);
        return list;
    }

    /**
     * 上传文件
     * 
     * @param request
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public void fileupload(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            if (request.getHeader("Content-Type") != null
                    && request.getHeader("Content-Type").startsWith(
                            "multipart/form-data")) {
                ServletFileUpload upload = new ServletFileUpload();

                FileItemIterator iterator = upload.getItemIterator(request);

                StringBuilder sb = new StringBuilder("{\"result\": [");

                while (iterator.hasNext()) {
                    sb.append("{");
                    FileItemStream item = iterator.next();
                    InputStream is = item.openStream();
                    File folder = new File("/home/ice/writeFile/");
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    File file = new File(folder, item.getName());
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    int b;
                    while ((b = is.read()) != -1) {
                        fos.write(b);
                    }
                    fos.close();
                    sb.append("\"fieldName\":\"").append(item.getFieldName())
                            .append("\",");
                    if (item.getName() != null) {
                        sb.append("\"name\":\"").append(item.getName())
                                .append("\",");
                    }
                    if (item.getName() != null) {
                        sb.append("\"size\":\"").append(size(is)).append("\"");
                    } else {
                        sb.append("\"value\":\"").append(read(is)).append("\"");
                    }
                    sb.append("}");
                    if (iterator.hasNext()) {
                        sb.append(",");
                    }
                    is.close();
                }
                sb.append("]}");
                response.setContentType("application/json");
                PrintWriter printWriter = new PrintWriter(
                        response.getOutputStream());
                try {
                    printWriter.print(sb.toString());
                    printWriter.flush();
                } finally {
                    printWriter.close();
                }
            } else {
                response.setContentType("application/json");
                PrintWriter printWriter = new PrintWriter(
                        response.getOutputStream());
                try {
                    printWriter.print("{\"result\":[{\"size\":"
                            + size(request.getInputStream()) + "}]}");
                    printWriter.flush();
                } finally {
                    printWriter.close();
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int size(InputStream stream) {
        int length = 0;
        try {
            byte[] buffer = new byte[2048];
            int size;
            while ((size = stream.read(buffer)) != -1) {
                length += size;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return length;
    }

    private String read(InputStream stream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    /**
     * 用户业务接口
     */
    @Autowired
    private IUserService userService;

}
