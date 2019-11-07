package com.deke.mall.logging;

import ch.qos.logback.core.rolling.RollingFileAppender;
import com.deke.mall.common.Constants;
import com.deke.mall.common.ParamKeys;
import org.apache.ibatis.parsing.GenericTokenParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author deke
 */
public class TraceNoRollingFileAppender<E> extends RollingFileAppender<E> {

    private GenericTokenParser genericTokenParser = new GenericTokenParser("#{","}",(String content)->{
        LocalDate localDate = LocalDate.now();
        if(ParamKeys.CURRENT_DATE.equals(content)){
            content = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }else {
            content = "#{"+content+"}";
        }
        return content;
    });

    @Override
    public void setFile(String file) {
        file = this.genericTokenParser.parse(file);
        if (file != null && !file.endsWith(Constants.INVALID_LOG)) {
            super.setFile(file);
        }
    }
}
