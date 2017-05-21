package com.scj.common.enums.handler;

import com.scj.common.enums.WebPageEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public class WebPageEnumHandler extends BaseTypeHandler<WebPageEnum>{

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, WebPageEnum webPageEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i,webPageEnum.ordinal());
    }

    @Override
    public WebPageEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return WebPageEnum.getEnumByCode(resultSet.getInt(s));
    }

    @Override
    public WebPageEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return WebPageEnum.getEnumByCode(resultSet.getInt(i));
    }

    @Override
    public WebPageEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return WebPageEnum.getEnumByCode(callableStatement.getInt(i));
    }
}
