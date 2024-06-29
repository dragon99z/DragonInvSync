package de.dragon99z.dragoninvsync.Func;

public class MyConfig {

    public General general;
    public Database database;
    public MySQL mysql;

    public static class General{
        public int syncDelay = 5;
    }

    public static class Database {
        public String type;
        public String name;
    }

    public static class MySQL {
        public String address;
        public int port = -1;
        public String username;
        public String password;
        public boolean debug;
    }

    public boolean validateBase() {
        return database != null && database.name != null && database.type != null;
    }

    public boolean validateMySQL() {
        return validateBase() && mysql != null && mysql.address != null && mysql.port !=-1 && mysql.username != null && mysql.password != null;
    }

}
