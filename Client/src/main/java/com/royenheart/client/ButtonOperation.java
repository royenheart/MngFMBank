package com.royenheart.client;

/**
 * GUI界面中每一个按钮对应的操作
 * <p>
 *     根据这些操作进行不同形式的动作
 * </p>
 * @author RoyenHeart
 */
public enum ButtonOperation {
    // 查询余额
    QueryBalance,
    // 取钱
    WithdrawMoney,
    // 存钱
    Saving,
    // 转账
    Transfer,
    // 修改用户信息
    ModifyUserInfo,
    // 开户
    CreateUser,
    // 销户
    DelUser,
    // 导入xls文件批量开户
    BatchCreateUser,
    // 获取年终报告（通过邮件的形式）
    GetReport,
    // 发送邮件
    SendMail,
    // 接收邮件
    ReceiveMail,
    // 将表单数据提交
    SendRequest,
    // 提交并将查询结果导出为xls文件
    SendRequestStore
}
