package vn.edu.hust.bluemoon.chung;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class KhongTimThayException extends RuntimeException {

    public KhongTimThayException(String thongBao) {
        super(thongBao);
    }
}
