package com.royenheart.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.royenheart.basicsets.programsettings.Planet;
import com.royenheart.basicsets.programsettings.SingleEvent;
import com.royenheart.basicsets.programsettings.User;
import com.royenheart.basicsets.programsettings.UserPattern;
import com.royenheart.server.atomics.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    synchronized public String queryMoney(ParseRequest parseRequest, Connection con, String tables, Planet planetSets) {
        try {
            if (Objects.equals(parseRequest.getRegAccountId(), "") || Objects.equals(parseRequest.getRegPasswd(), "")) {
                System.err.println("accountId或password字段不存在");
                return "请求accountId或password字段不存在";
            }

            // 查询人密码是否匹配
            LinkedList<HashMap<String, String>> query = new AtomicQueryPasswd(con, tables,
                    parseRequest.getRegAccountId()).query();
            if (!query.getFirst().get(String.valueOf(UserPattern.password)).equals(parseRequest.getRegPasswd())) {
                System.err.println("密码不匹配，无法进行转账");
                return "密码不匹配，无法进行查询";
            }

            query = new AtomicQueryMoney(con, tables, parseRequest.getRegAccountId()).query();
            return "当前余额:" + query.getFirst().get("money");
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        } catch (NoSuchElementException e) {
            System.err.println("查询人ID不存在");
            e.printStackTrace();
            return "查询人ID不存在";
        }
    }

    /**
     * 取钱
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return 取钱数据
     */
    synchronized public String getMoney(ParseRequest parseRequest, Connection con, String tables, Planet planetSets) {
        try {
            if (Objects.equals(parseRequest.getRegAccountId(), "") || Objects.equals(parseRequest.getRegMoney(), "")) {
                System.err.println("accountId或money字段缺失");
                return "accountId或money字段缺失";
            } else if (Objects.equals(parseRequest.getRegPasswd(), "")) {
                System.err.println("password缺失");
                return "password缺失";
            }

            // 查询取钱人密码是否匹配
            LinkedList<HashMap<String, String>> query = new AtomicQueryPasswd(con, tables,
                    parseRequest.getRegAccountId()).query();
            if (!query.getFirst().get(String.valueOf(UserPattern.password)).equals(parseRequest.getRegPasswd())) {
                System.err.println("密码不匹配，无法进行转账");
                return "密码不匹配，无法取钱";
            }

            query = new AtomicQueryMoney(con,
                    tables, parseRequest.getRegAccountId()).query();
            double currentMoney = Double.parseDouble(query.getFirst().get("money"));
            double fetchMoney = Double.parseDouble(parseRequest.getRegMoney());
            if (currentMoney < fetchMoney) {
                return "当前余额: " + currentMoney + "，无法取钱";
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
        } catch (NoSuchElementException e) {
            System.err.println("取款人ID不存在");
            e.printStackTrace();
            return "取款人ID不存在";
        }
    }

    /**
     * 存钱操作
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return 返回更新的信息
     */
    synchronized public String saveMoney(ParseRequest parseRequest, Connection con, String tables, Planet planetSets) {
        try {
            if (Objects.equals(parseRequest.getRegAccountId(), "") || Objects.equals(parseRequest.getRegMoney(), "")) {
                System.err.println("accountId或money字段缺失");
                return "accountId或money字段缺失";
            } else if (Objects.equals(parseRequest.getRegPasswd(), "")) {
                System.err.println("password缺失");
                return "password缺失";
            }

            // 查询存钱人密码是否匹配
            LinkedList<HashMap<String, String>> query = new AtomicQueryPasswd(con, tables,
                    parseRequest.getRegAccountId()).query();
            if (!query.getFirst().get(String.valueOf(UserPattern.password)).equals(parseRequest.getRegPasswd())) {
                System.err.println("密码不匹配，无法进行转账");
                return "密码不匹配，无法取钱";
            }

            query = new AtomicQueryMoney(con, tables,
                    parseRequest.getRegAccountId()).query();
            double currentMoney = Double.parseDouble(query.getFirst().get("money"));
            double putMoney = Double.parseDouble(parseRequest.getRegMoney());
            if (currentMoney + putMoney > User.MAX_MONEY) {
                return "存放后余额将为: " + (currentMoney + putMoney) + "，无法进行存钱";
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
        } catch (NoSuchElementException e) {
            System.err.println("存款人ID不存在");
            e.printStackTrace();
            return "存款人ID不存在";
        }
    }

    /**
     * 转账
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return 数据库查询信息
     */
    synchronized public String transferMoney(ParseRequest parseRequest,
                                             Connection con, String tables, Planet planetSets) {
        LinkedList<String> mulAccountId = parseRequest.getRegMulAccountId();
        if (mulAccountId == null || mulAccountId.size() != 2) {
            System.err.println("转账所需accountid字段不等于2个");
            return "转出人和收账人错误";
        } else if (Objects.equals(parseRequest.getRegMoney(), "")) {
            System.err.println("未指定钱数");
            return "未指定钱数";
        } else if (Objects.equals(parseRequest.getRegPasswd(), "")) {
            System.err.println("转出人密码未提供");
            return "转出人密码未提供";
        }

        try {
            // 查询转出人密码是否匹配
            LinkedList<HashMap<String, String>> query = new AtomicQueryPasswd(con, tables,
                    mulAccountId.get(0)).query();
            if (!query.getFirst().get(String.valueOf(UserPattern.password)).equals(parseRequest.getRegPasswd())) {
                System.err.println("密码不匹配，无法进行转账");
                return "转出人密码不匹配，无法进行转账";
            }

            query = new AtomicQueryMoney(con, tables, mulAccountId.get(0)).query();
            double outCurrentMoney = Double.parseDouble(query.getFirst().get("money"));

            query = new AtomicQueryMoney(con, tables, mulAccountId.get(1)).query();
            double inCurrentMoney = Double.parseDouble(query.getFirst().get("money"));

            double transferMoney = Double.parseDouble(parseRequest.getRegMoney());
            if (transferMoney > outCurrentMoney) {
                return "转出人钱数不够";
            } else if (transferMoney + inCurrentMoney > User.MAX_MONEY) {
                return "转入人钱数将超出上限，请联系转入人或减少转账额度";
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
        } catch (NoSuchElementException e) {
            System.err.println("转出人或转入人ID不存在");
            e.printStackTrace();
            return "转出人或转入人ID不存在";
        }
    }

    /**
     * 修改用户信息（手机号，名字，死亡情况，继承人）
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return 记录更新信息
     */
    synchronized public String editUser(ParseRequest parseRequest, Connection con, String tables, Planet planetSets) {
        try {
            if (Objects.equals(parseRequest.getRegAccountId(), "") || Objects.equals(parseRequest.getRegPasswd(), "")) {
                System.err.println("需要修改人账号ID和密码");
                return "需要提供账号ID和对应密码";
            } else if (Objects.equals(parseRequest.getRegName(), "") &&
                    Objects.equals(parseRequest.getRegPhone(), "") &&
                    Objects.equals(parseRequest.getRegDeath(), "") &&
                    Objects.equals(parseRequest.getRegHeir(), "")) {
                System.err.println("至少提供手机号、名字、死亡状态、继承人中的一个");
                return "至少提供手机号、名字、死亡状态、继承人中的一个";
            }

            // 修改人提供密码是否匹配
            LinkedList<HashMap<String, String>> query = new AtomicQueryPasswd(con, tables,
                    parseRequest.getRegAccountId()).query();
            if (!query.getFirst().get(String.valueOf(UserPattern.password)).equals(parseRequest.getRegPasswd())) {
                System.err.println("密码不匹配，信息无法进行修改");
                return "密码不匹配，信息无法进行修改";
            }

            HashMap<String, String> updates = new HashMap<>();
            if (!Objects.equals(parseRequest.getRegName(), "")) { updates.put(String.valueOf(UserPattern.name),
                    parseRequest.getRegName()); }
            if (!Objects.equals(parseRequest.getRegPhone(), "")) { updates.put(String.valueOf(UserPattern.phone),
                    parseRequest.getRegPhone()); }
            if (!Objects.equals(parseRequest.getRegDeath(), "")) { updates.put(String.valueOf(UserPattern.death),
                    parseRequest.getRegDeath()); }
            if (!Objects.equals(parseRequest.getRegHeir(), "")) { updates.put(String.valueOf(UserPattern.heir),
                    parseRequest.getRegHeir()); }

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
        } catch (NoSuchElementException e) {
            System.err.println("待修改用户ID不存在");
            e.printStackTrace();
            return "待修改用户ID不存在";
        }
    }

    /**
     * 用户密码更改
     * @param parseRequest 用户请求解析其器
     * @param con 数据库连接
     * @param tables 使用数据表（默认Users）
     * @param planetSets 行星数据
     * @return 记录更新信息
     */
    synchronized public String editPasswd(ParseRequest parseRequest, Connection con, String tables, Planet planetSets) {
        try {
            LinkedList<String> passwords;
            passwords = parseRequest.getRegMulPassword();
            if (passwords == null || passwords.isEmpty() || Objects.equals(parseRequest.getRegAccountId(), "")
                    || Objects.equals(parseRequest.getRegTable(), "")) {
                System.err.println("用户请求字段缺失");
                return "用户请求字段缺失";
            }

            // 修改人提供密码是否匹配
            LinkedList<HashMap<String, String>> query = new AtomicQueryPasswd(con, parseRequest.getRegTable(),
                    parseRequest.getRegAccountId()).query();
            if (!query.getFirst().get(String.valueOf(UserPattern.password)).equals(passwords.get(0))) {
                System.err.println("原密码不匹配，身份认证失败");
                return "原密码不匹配";
            }

            HashMap<String, String> updates = new HashMap<>();
            updates.put(String.valueOf(UserPattern.password), passwords.get(1));

            boolean success = new AtomicUpdateUser(con, parseRequest.getRegTable(),
                    parseRequest.getRegAccountId(), updates).update();
            if (success) {
                return "密码修改成功";
            } else {
                return "密码修改失败，请检查合法性";
            }
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        } catch (NoSuchElementException e) {
            System.err.println("待修改用户ID不存在");
            e.printStackTrace();
            return "待修改用户ID不存在";
        }
    }

    /**
     * 添加用户操作
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return 返回更新的信息
     */
    synchronized public String addUser(ParseRequest parseRequest, Connection con, String tables, Planet planetSets) {
        try {
            if (parseRequest.allPatterns()) {
                System.err.println("用户请求字段缺失");
                return "用户请求字段缺失";
            }

            // 查询用户是否已经存在
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
                BankData.addNewUsers(planetSets.getYear(), 1);
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
    synchronized public String delUser(ParseRequest parseRequest, Connection con, String tables, Planet planetSets) {
        if (Objects.equals(parseRequest.getRegAccountId(), "")) {
            System.err.println("未指定删除用户");
            return "未指定删除用户";
        } else if (Objects.equals(parseRequest.getRegPasswd(), "")) {
            System.err.println("password缺失");
            return "password缺失";
        }

        try {
            // 修改人提供密码是否匹配
            LinkedList<HashMap<String, String>> query = new AtomicQueryPasswd(con, tables,
                    parseRequest.getRegAccountId()).query();
            if (!query.getFirst().get(String.valueOf(UserPattern.password)).equals(parseRequest.getRegPasswd())) {
                System.err.println("密码不匹配，无法进行转账");
                return "密码不匹配，无法取钱";
            }

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
        } catch (NoSuchElementException e) {
            System.err.println("待销户人ID不存在");
            e.printStackTrace();
            return "待销户人ID不存在";
        }
    }

    /**
     * 用户登录，分普通用户登录和管理员用户登录，需要先使用请求解析引擎确定使用哪个数据表
     * @param parseRequest 客户端请求解析器
     * @param con 数据库连接
     * @param tables 数据表
     * @return 返回登录的信息，之后对应线程的登录状态设置为真
     */
    synchronized public String login(ParseRequest parseRequest, Connection con, String tables, Planet planetSets) {
        if (!Objects.equals(parseRequest.getRegTable(), "")) {
            tables = parseRequest.getRegTable();
        } else if (Objects.equals(parseRequest.getRegAccountId(), "")
                || Objects.equals(parseRequest.getRegPasswd(), "")) {
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
        } catch (NoSuchElementException e) {
            System.err.println("请求登录密码出错，已拒绝");
            e.printStackTrace();
            return "登录失败";
        }
    }

    /**
     * 通过xls文件批量开户
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return 数据库查询信息
     */
    synchronized public String xlsCreate(ParseRequest parseRequest, Connection con, String tables, Planet planetSets) {
        File file = new File("./createRequest.xls");
        try {
            readFile(file, parseRequest);
        } catch (FileNotFoundException e) {
            return "空请求/服务器出现未知错误，请联系管理员";
        } catch (IOException e) {
            return "服务器读写错误，请联系管理员";
        }

        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             POIFSFileSystem fileSystem = new POIFSFileSystem(bufferedInputStream);
             HSSFWorkbook workbook = new HSSFWorkbook(fileSystem)) {
            HSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.rowIterator();
            if (rows.hasNext()) {
                Iterator<Cell> cells = rows.next().cellIterator();
                LinkedList<String> patterns = new LinkedList<>();
                // 获取各列对应的字段
                while (cells.hasNext()) {
                    patterns.add(cells.next().getStringCellValue());
                }

                ArrayList<Integer> errors = new ArrayList<>();
                LinkedList<User> users = cellsGetUser(rows, patterns, errors);

                // 原子操作批量开户
                boolean success = new AtomicCreateUser(con, tables, users).createMul();
                boolean rm = file.delete();
                if (success && errors.isEmpty() && rm ) {
                    BankData.addNewUsers(planetSets.getYear(), users.size() - errors.size());
                    return "批量开户成功";
                } else if (!(success && errors.isEmpty())) {
                    StringBuilder errMsg = new StringBuilder("批量开户失败，请检查\n");
                    for (Integer tmp : errors) {
                        errMsg.append("在").append(tmp).append("行上出现错误\n");
                    }
                    return String.valueOf(errMsg);
                } else {
                    return "服务端IO资源出错，请联系管理员";
                }
            } else {
                System.err.println("空excel表");
                return "空excel表";
            }
        } catch (IOException e) {
            System.err.println("excel文件打开失败");
            e.printStackTrace();
            return "文件打开失败";
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        }
    }

    /**
     * 根据字节数组列表字符串接收文件
     * @param file 写出的文件（由调用者指定）
     * @param parseRequest 解析出的字节数组列表解析器
     * @throws FileNotFoundException 空请求或者临时文件不存在
     * @throws IOException 读写失败
     */
    synchronized private void readFile(File file, ParseRequest parseRequest) throws
            FileNotFoundException, IOException {
        try (FileOutputStream excelStream = new FileOutputStream(file);
             DataOutputStream out = new DataOutputStream(excelStream)) {
            Gson gson = new Gson();

            LinkedList<String> fileBytes = parseRequest.getRegMulBytes();
            if (fileBytes == null) {
                System.err.println("空文件");
                throw new FileNotFoundException("空文件");
            }

            for (String bytes : fileBytes) {
                byte[] temp = gson.fromJson(bytes, new TypeToken<byte[]>() {}.getType());
                out.write(temp);
            }
        } catch (FileNotFoundException e) {
            System.err.println("创建文件未找到");
            throw e;
        } catch (IOException e) {
            System.err.println("文件接收失败");
            throw e;
        }
    }

    /**
     * 将文件按字节数组列表导出
     * @param file 需要导出的文件
     * @return 文件对应的字节数组列表
     */
    synchronized private String writeFile(File file) {
        byte[] data = new byte[300];
        StringBuilder response = new StringBuilder();
        Gson gson = new Gson();

        try (FileInputStream inputStream = new FileInputStream(file);
             DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            while (dataInputStream.read(data) > 0) {
                String js = gson.toJson(data).replace(" ", "");
                response.append(js);
            }
        } catch (IOException e) {
            System.err.println("文件未找到");
            e.printStackTrace();
            return null;
        }
        return String.valueOf(response);
    }

    /**
     * 读取一行单元格生成伪请求，交给ParseRequest解析字段，生成用户
     * @param rows 行迭代器
     * @param patterns 各列对应字段
     * @param errors 错误行
     * @return 用户列表（链表形式）
     */
    synchronized private LinkedList<User> cellsGetUser(Iterator<Row> rows, LinkedList<String> patterns,
                                                       ArrayList<Integer> errors) {
        LinkedList<User> users = new LinkedList<>();

        // 迭代各行，每行生成一个用户，加到用户名单上
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            StringBuilder userPatterns = new StringBuilder().append("F%");
            Iterator<Cell> cells = currentRow.cellIterator();
            // 遍历各单元格对应列是哪个字段
            ListIterator<String> pattern = patterns.listIterator(0);
            while (cells.hasNext()) {
                Cell cell = cells.next();
                String value = "";
                if (cell.getCellType() == CellType.NUMERIC) {
                    value = NumberToTextConverter.toText(cell.getNumericCellValue());
                } else if (cell.getCellType() == CellType.STRING) {
                    value = cell.getStringCellValue();
                }
                userPatterns.append(String.format("%s:%s;", pattern.next(), value));
            }
            userPatterns.append("%");
            ParseRequest parseRequest = new ParseRequest(String.valueOf(userPatterns));
            try {
                users.add(new User(parseRequest.getRegAge(), parseRequest.getRegSexString(), parseRequest.getRegName(),
                        parseRequest.getRegPasswd(), parseRequest.getRegPhone(), parseRequest.getRegMoney(),
                        parseRequest.getRegDeath(), parseRequest.getRegBirth(), parseRequest.getRegPersonalId(),
                        parseRequest.getRegAccountId(), parseRequest.getRegHeir()));
            } catch (ParseException e) {
                errors.add(currentRow.getRowNum());
                System.err.println(currentRow.getRowNum() + "行用户字段解析失败，已丢弃");
                e.printStackTrace();
            }
        }
        return users;
    }


    /**
     * 将特定查询信息导出为xls文件，先查询，再根据查询结果创建xls文件对象，最后返回给客户端
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return xls文件的字节数组列表字符串，交给客户端，客户端解析之后进行文件的转换
     */
    synchronized public String queryXls(ParseRequest parseRequest, Connection con, String tables, Planet planetSets) {
        File file = new File("./queryOut.xls");
        LinkedList<HashMap<String, String>> query;

        try {
            query = new AtomicQueryAll(con, tables, parseRequest).query("AND");
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             HSSFWorkbook workbook = new HSSFWorkbook()) {

            // 创建数据表
            int rowIndex = 0;
            HSSFSheet sheet = workbook.createSheet();
            HSSFRow row = sheet.createRow(rowIndex);
            row.createCell(0).setCellValue(String.valueOf(UserPattern.accountId));
            row.createCell(1).setCellValue(String.valueOf(UserPattern.personalId));
            row.createCell(2).setCellValue(String.valueOf(UserPattern.name));
            row.createCell(3).setCellValue(String.valueOf(UserPattern.age));
            row.createCell(4).setCellValue(String.valueOf(UserPattern.sex));
            row.createCell(5).setCellValue(String.valueOf(UserPattern.password));
            row.createCell(6).setCellValue(String.valueOf(UserPattern.phone));
            row.createCell(7).setCellValue(String.valueOf(UserPattern.money));
            row.createCell(8).setCellValue(String.valueOf(UserPattern.death));
            row.createCell(9).setCellValue(String.valueOf(UserPattern.birth));
            row.createCell(10).setCellValue(String.valueOf(UserPattern.heir));

            for (HashMap<String, String> stringStringHashMap : query) {
                rowIndex += 1;
                row = sheet.createRow(rowIndex);
                for (int i = 0; i <= 10; i++) {
                    row.createCell(i).setCellValue(
                            stringStringHashMap.get(sheet.getRow(0).getCell(i).getStringCellValue()));
                }
            }

            workbook.write(fileOutputStream);
        } catch (FileNotFoundException e) {
            System.err.println("创建文件未找到");
            e.printStackTrace();
            return "服务端错误，请联系管理员";
        } catch (IOException e) {
            System.err.println("文件接收失败");
            e.printStackTrace();
            return "服务端错误，请联系管理员";
        }

        String result = writeFile(file);

        if (result != null) {
            System.out.println("查询信息导出xls文件成功");
            return result;
        } else {
            System.err.println("导出xls文件失败");
            return "服务端导出xls文件错误，请检查";
        }
    }

    /**
     * 更新行星信息（部分信息已经被修改）（不对客户端开放）
     * <p>
     *     需要进行的更新操作（按顺序来）
     *     1. 计算利息并加到本金上
     *     2. 更新所有人年龄
     *     3. 查询所有年龄在70岁以上或者处于死亡状态的，加入销户名单的同时加入其继承人
     *     4. 对所有待销户用户进行销户和转账操作，所有被销户的用户将财产转移至继承人，若没有继承人，转移给银行
     * </p>
     * @param con 数据库连接
     * @param tables 需要更新的数据表
     * @param interest 利息率
     * @param yearPass 是否过了一年，是为真
     * @return 是否成功执行操作
     */
    synchronized public String updatePlanet(Connection con, String tables, double interest,
                                            boolean yearPass, Planet planetSets) {
        StringBuilder result = new StringBuilder();

        try {
            AtomicUpdateMoney o1 = new AtomicUpdateMoney(con, tables, String.valueOf(interest));
            boolean r1 = o1.updateAllBasedOnPrevious();

            if (!r1) {
                result.append("本金利息数据更新失败\n");
            } else {
                result.append("本金利息数据更新成功\n");
            }

            // 若是过了一年就更新所有人的年龄（+1）
            if (yearPass) {
                AtomicUpdateUser o2 = new AtomicUpdateUser(con, tables);
                boolean r2 = o2.updateAgeAll();
                if (!r2) {
                    result.append("年龄更新失败\n");
                } else {
                    result.append("年龄更新成功\n");
                }
            }

            // 查询需要销户的人
            LinkedList<HashMap<String, String>> query = new AtomicQueryAll(con, tables,
                    new ParseRequest("I%death:= TRUE;age:>=70;%")).queryAgeAndDeath("OR");

            // 指定继承人进行财产的继承，同时进行销户操作
            for (HashMap<String, String> user : query) {
                result.append(transferMoney(new ParseRequest("D%accountId:" + user.get("accountId") + ";accountId:" +
                                      user.get("heir") + ";password:" + user.get("password") + ";%"),
                              con, tables, planetSets))
                      .append("\n");
                result.append(delUser(new ParseRequest("G%accountId:" + user.get("accountId") + ";%"),
                              con, tables, planetSets))
                      .append("\n");
            }

            return String.valueOf(result);
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        }
    }

    /**
     * 生成年终报告并返回给客户端
     * <p>
     *     报告内容包括：
     *     1. 星球当前时间
     *     2. 利息变更记录
     *     3. 账户总数
     *     4. 今年新开户数
     *     5. 总余额数据
     *     6. 发生的事件
     * </p>
     * @param parseRequest 客户端请求解析
     * @param con 数据库连接
     * @param tables 数据表
     * @return 数据库查询信息
     */
    synchronized public String queryYearlyReport(ParseRequest parseRequest, Connection con,
                                                 String tables, Planet planetSets) {
        File file = new File("./report" + new SimpleDateFormat("yyyy").format(new Date()) + ".pdf");

        try {
            boolean rm = true;
            if (file.exists()) {
                rm = file.delete();
            }

            if (!rm) {
                System.err.println("删除旧报告文件错误，请检查服务器IO状态");
                return "服务器IO错误，请联系管理员";
            }

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            document.addTitle("Year Report");
            document.add(new Paragraph("Planet Time : " + planetSets.getPlanetTime()));

            LinkedList<Double> interests = BankData.getInterest(planetSets.getYear());
            StringBuilder value = new StringBuilder();
            value.append("Yearly Interest changes: \n");
            for (Double interest : interests) {
                value.append(interest).append("-->\n");
            }
            document.add(new Paragraph(String.valueOf(value)));

            LinkedList<HashMap<String, String>> query = new AtomicQueryAll(con, tables, parseRequest).queryOneForAll("money");
            document.add(new Paragraph("Account Amounts : " + query.size()));
            BigDecimal sum = new BigDecimal(0);
            for (HashMap<String, String> record : query) {
                sum = sum.add(BigDecimal.valueOf(Double.parseDouble(record.get("money"))));
            }
            document.add(new Paragraph("Money Sum : " + sum));
            document.add(new Paragraph("Yearly new Accounts : " + BankData.getNewUsers(planetSets.getYear())));

            LinkedList<SingleEvent> events = BankData.getEvents(planetSets.getYear());
            value = new StringBuilder();
            value.append("Things Happen this year : \n");
            for (SingleEvent event : events) {
                value.append(event.getDescription()).append("\n");
            }
            document.add(new Paragraph(String.valueOf(value)));
            document.close();

            return writeFile(file);
        } catch (DocumentException | FileNotFoundException e) {
            System.err.println("PDF生成错误，请检查");
            e.printStackTrace();
            return "PDF生成错误，请联系管理员";
        } catch (SQLException e) {
            System.err.println("数据库请求失败");
            e.printStackTrace();
            return "数据库请求失败";
        }
    }

}
