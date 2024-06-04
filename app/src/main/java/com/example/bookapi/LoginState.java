package com.example.bookapi;

public class LoginState {
    public static final class Loading extends LoginState {}
    public static final class Success extends LoginState {
        private  String o_u;
        private  String sesskey;

        public String getO_u() {
            return o_u;
        }

        public String getSesskey() {
            return sesskey;
        }

        public Success(String o_u, String sesskey) {
            this.o_u = o_u;
            this.sesskey = sesskey;
        }


    }
    public static final class Error extends LoginState {
        private final Exception exception;
        public Error(Exception exception) {
            this.exception = exception;
        }

        public Exception getException() {
            return exception;
        }
    }
}
