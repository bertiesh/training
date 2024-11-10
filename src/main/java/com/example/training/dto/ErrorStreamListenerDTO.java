package com.example.training.dto;

import cn.hutool.core.io.IoUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;

/**
 * @author Xinyuan Xu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class ErrorStreamListenerDTO implements Runnable{
    private String videoURI;

    private BufferedReader reader;

    @Override
    public void run() {
        try {
            String msg=null;
            while ((msg=reader.readLine())!=null){
                log.error("Push exception message -->"+msg);
            }
        }catch (Exception e){

        }finally {
            IoUtil.close(reader);
        }

    }
}
