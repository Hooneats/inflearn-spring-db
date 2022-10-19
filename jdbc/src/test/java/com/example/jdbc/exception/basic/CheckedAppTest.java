package com.example.jdbc.exception.basic;

import java.net.ConnectException;
import java.sql.SQLException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 체크예외의 문제
 * 1. 대부분의 예외는 복구가 불가능하다. 연결문제거나 문법이 안맞거나 복구와 관련없는게 대부분
 * 2. 의존 관계에 대한 문제 이는 다른계층에서 의존하게 된다.
 */
public class CheckedAppTest {

    @Test
    void checked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request())
            .isInstanceOf(Exception.class);
    }

    static class Controller {
        Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.login();
        }
    }


    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void login() throws SQLException, ConnectException {
            repository.call();
            networkClient.call();
        }

    }

    static class NetworkClient {
        public void call() throws ConnectException {
            throw new ConnectException();
        }
    }

    static class Repository {
        public void call() throws SQLException {
            throw new SQLException();
        }
    }

}
