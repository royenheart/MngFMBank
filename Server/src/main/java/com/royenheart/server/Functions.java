package com.royenheart.server;

import com.royenheart.basicsets.programsettings.User;
import com.royenheart.server.atomics.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * 利用锁机制实现各种功能
 * @author RoyenHeart
 */
public class Functions {

    // 使用单例设计模式

    private static final Functions ME = new Functions();
    private Functions() {}

    public static Functions getMe() {
        return ME;
    }

    /**
     * 查询余额
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @return 结果字符串，以Json或者普通字符串格式发送
     */
    synchronized public String queryMoney(ParseRequest parseRequest, Connection con, String tables, boolean login) {
        if (!login) {
            return "您尚未登录!";
        }

        try {
            if (parseRequest.getRegAccountId() == null) {
                System.err.println("accountId字段不存在");
                return "请求accountId字段不存在";
            }

            LinkedList<HashMap<String, String>> query = new AtomicQueryMoney(con,
                    tables, parseRequest.getRegAccountId()).query();
            return "当前余额:" + query.getFirst().get("money");
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        }
    }

    /**
     * 取钱
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return 取钱数据
     */
    synchronized public String getMoney(ParseRequest parseRequest, Connection con, String tables, boolean login) {
        if (!login) {
            return "您尚未登录!";
        }

        try {
            if (parseRequest.getRegAccountId() == null || parseRequest.getRegMoney() == null) {
                System.err.println("accountId或money字段缺失");
                return "accountId或money字段缺失";
            }

            LinkedList<HashMap<String, String>> query = new AtomicQueryMoney(con,
                    tables, parseRequest.getRegAccountId()).query();
            double currentMoney = Double.parseDouble(query.getFirst().get("money"));
            double fetchMoney = Double.parseDouble(parseRequest.getRegMoney());
            if (currentMoney < fetchMoney) {
                return "当前余额: " + currentMoney + ",您没有这么多钱";
            }

            boolean success = new AtomicUpdateMoney(con, tables, parseRequest.getRegAccountId(),
                    String.valueOf(currentMoney - fetchMoney)).update();
            if (success) {
                return "取钱" + fetchMoney + "成功，当前余额" + (currentMoney - fetchMoney);
            } else {
                return "取钱失败";
            }
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        }
    }

    /**
     * 存钱操作
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return 返回更新的信息
     */
    synchronized public String saveMoney(ParseRequest parseRequest, Connection con, String tables, boolean login) {
        if (!login) {
            return "您尚未登录!";
        }

        try {
            if (parseRequest.getRegAccountId() == null || parseRequest.getRegMoney() == null) {
                System.err.println("accountId或money字段缺失");
                return "accountId或money字段缺失";
            }

            LinkedList<HashMap<String, String>> query = new AtomicQueryMoney(con, tables,
                    parseRequest.getRegAccountId()).query();
            double currentMoney = Double.parseDouble(query.getFirst().get("money"));
            double putMoney = Double.parseDouble(parseRequest.getRegMoney());
            if (currentMoney + putMoney > User.MAX_MONEY) {
                return "存放后余额将为: " + (currentMoney + putMoney) + ",超出存放上限，请少存点";
            }

            boolean success = new AtomicUpdateMoney(con, tables, parseRequest.getRegAccountId()
                    , String.valueOf(currentMoney + putMoney)).update();
            if (success) {
                return "存钱" + putMoney + "成功，当前余额" + (currentMoney + putMoney);
            } else {
                return "存钱失败";
            }
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        }
    }

    /**
     * 转账
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return 数据库查询信息
     */
    synchronized public String transferMoney(ParseRequest parseRequest, Connection con, String tables, boolean login) {
        if (!login) {
            return "您尚未登录!";
        }

        LinkedList<String> mulAccountId = parseRequest.getRegMulAccountId();
        if (mulAccountId == null || mulAccountId.size() != 2) {
            System.err.println("转账所需accountid字段不等于2个");
            return "转出人和收账人错误";
        } else if (parseRequest.getRegMoney() == null) {
            System.err.println("未指定钱数");
            return "未指定钱数";
        }

        try {
            System.out.println("从用户" + mulAccountId.get(0) + "查询");
            LinkedList<HashMap<String, String>> query = new AtomicQueryMoney(con,
                    tables, mulAccountId.get(0)).query();
            double outCurrentMoney = Double.parseDouble(query.getFirst().get("money"));

            System.out.println("从用户" + mulAccountId.get(1) + "查询");
            query = new AtomicQueryMoney(con, tables, mulAccountId.get(1)).query();
            double inCurrentMoney = Double.parseDouble(query.getFirst().get("money"));

            double transferMoney = Double.parseDouble(parseRequest.getRegMoney());
            if (transferMoney > outCurrentMoney) {
                return "转出人钱数不够";
            } else if (transferMoney + inCurrentMoney > User.MAX_MONEY) {
                return "转入人钱数将超出上线，请转少一点";
            } else {
                boolean success1 = new AtomicUpdateMoney(con, tables, mulAccountId.get(0),
                        String.valueOf(outCurrentMoney - transferMoney)).update();

                boolean success2 = new AtomicUpdateMoney(con, tables, mulAccountId.get(1),
                        String.valueOf(inCurrentMoney + transferMoney)).update();

                boolean success = success1 && success2;
                if (success) {
                    return String.format("成功从%s转出%s至%s\n%s当前余额:%s\n%s当前余额:%s",
                            mulAccountId.get(0), transferMoney, mulAccountId.get(1), mulAccountId.get(0),
                            outCurrentMoney - transferMoney, mulAccountId.get(1), inCurrentMoney + transferMoney);
                } else {
                    return "转账失败";
                }
            }
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        }
    }

    /**
     * 修改用户信息
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return 记录更新信息
     */
    synchronized public String editUser(ParseRequest parseRequest, Connection con, String tables, boolean login) {
        if (!login) {
            return "您尚未登录!";
        }

        try {
            if (parseRequest.getRegAccountId() == null && (parseRequest.getRegMoney() == null ||
                    parseRequest.getRegAge() == null || parseRequest.getRegSexString() == null ||
                    parseRequest.getRegName() == null || parseRequest.getRegPasswd() == null ||
                    parseRequest.getRegPhone() == null || parseRequest.getRegDeath() == null ||
                    parseRequest.getRegBirth() == null || parseRequest.getRegPersonalId() == null ||
                    parseRequest.getRegHeir() == null)) {
                System.err.println("请求字段缺失，至少需要accountid和用户信息字段中的一个");
                return "请求字段缺失，至少需要accountid和用户信息字段中的一个";
            }

            HashMap<String, String> updates = new HashMap<>();
            if (parseRequest.getRegName() != null) { updates.put("name", parseRequest.getRegName()); }
            if (parseRequest.getRegPasswd() != null) { updates.put("password", parseRequest.getRegPasswd()); }
            if (parseRequest.getRegPhone() != null) { updates.put("phone", parseRequest.getRegPhone()); }
            if (parseRequest.getRegDeath() != null) { updates.put("death", parseRequest.getRegDeath()); }
            if (parseRequest.getRegHeir() != null) { updates.put("heir", parseRequest.getRegHeir()); }


            boolean success = new AtomicUpdateUser(con, tables, parseRequest.getRegAccountId(), updates).update();
            if (success) {
                return "用户信息修改成功";
            } else {
                return "用户信息修改失败，请检查合法性";
            }
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        }
    }

    /**
     * 添加用户操作
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return 返回更新的信息
     */
    synchronized public String addUser(ParseRequest parseRequest, Connection con, String tables, boolean login) {
        if (!login) {
            return "您尚未登录!";
        }

        try {
            if (parseRequest.getRegAccountId() == null || parseRequest.getRegMoney() == null ||
                    parseRequest.getRegAge() == null || parseRequest.getRegSexString() == null ||
                    parseRequest.getRegName() == null || parseRequest.getRegPasswd() == null ||
                    parseRequest.getRegPhone() == null || parseRequest.getRegDeath() == null ||
                    parseRequest.getRegBirth() == null || parseRequest.getRegPersonalId() == null ||
                    parseRequest.getRegHeir() == null) {
                System.err.println("请求字段缺失");
                return "请求字段缺失";
            }

            // 查询用户表
            LinkedList<HashMap<String, String>> query = new AtomicQueryName(con, tables,
                    parseRequest.getRegAccountId()).query();
            try {
                String hasName = query.getFirst().get("name");
                if (hasName != null) {
                    return "已存在用户" + hasName;
                }
            } catch (NoSuchElementException e) {
                System.out.println("用户未存在，可进行添加");
            }

            User user = new User(parseRequest.getRegAge(), parseRequest.getRegSexString(), parseRequest.getRegName(),
                    parseRequest.getRegPasswd(), parseRequest.getRegPhone(), parseRequest.getRegMoney(),
                    parseRequest.getRegDeath(), parseRequest.getRegBirth(), parseRequest.getRegPersonalId(),
                    parseRequest.getRegAccountId(), parseRequest.getRegHeir());

            boolean success = new AtomicCreateUser(con, tables, user).createSingle();
            if (success) {
                return user.getName() + "用户添加成功";
            } else {
                return "添加失败，请检查";
            }
        } catch (ParseException e) {
            System.err.println("请求字段解析失败");
            e.printStackTrace();
            return "请求字段解析失败";
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        }
    }

    /**
     * 删除用户
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return 返回更新的信息
     */
    synchronized public String delUser(ParseRequest parseRequest, Connection con, String tables, boolean login) {
        if (!login) {
            return "您尚未登录!";
        }

        if (parseRequest.getRegAccountId() == null) {
            System.err.println("未指定删除用户");
            return "未指定删除用户";
        }

        try {
            boolean success = new AtomicDelUser(con, tables, parseRequest.getRegAccountId()).delete();
            if (success) {
                return "用户删除成功";
            } else {
                return "删除失败，请检查";
            }
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        }
    }

    /**
     * 用户登录，分普通用户登录和管理员用户登录，需要先使用请求解析引擎确定使用哪个数据表
     * @param parseRequest 客户端请求解析器
     * @param con 数据库连接
     * @param tables 数据表
     * @return 返回登录的信息，之后对应线程的登录状态设置为真
     */
    synchronized public String login(ParseRequest parseRequest, Connection con, String tables, boolean login) {
        if (login) {
            return "请勿重复登录";
        }

        if (parseRequest.getRegTable() != null) {
            tables = parseRequest.getRegTable();
        } else if (parseRequest.getRegAccountId() == null || parseRequest.getRegPasswd() == null) {
            System.err.println("用户名或者密码字段缺失");
            return "用户名或者密码缺失，请检查表单";
        }

        try {
            LinkedList<HashMap<String, String>> query = new AtomicQueryPasswd(con, tables,
                    parseRequest.getRegAccountId()).query();
            String databasePasswd = query.getFirst().get("password");

            if (databasePasswd.equals(parseRequest.getRegPasswd())) {
                return "true";
            } else {
                return "false";
            }
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        }
    }

}
