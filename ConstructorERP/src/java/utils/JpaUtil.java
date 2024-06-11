package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {

    private static JpaUtil instance;
    private EntityManagerFactory emf;
    private static final Logger logger = Logger.getLogger(JpaUtil.class.getName());

    private JpaUtil() {
    }

    public static JpaUtil get() {
        if (instance == null) {
            instance = new JpaUtil();
        }
        return instance;
    }

    public EntityManager getEntityManager() throws Exception {
        if (emf == null || !emf.isOpen()) {
            logger.info("JpaUtil.getEntityManager antes de createEntityManagerFactory(\"SistemaM7PU\") ");
            try {
                Class.forName("com.mysql.jdbc.Driver");
                emf = Persistence.createEntityManagerFactory("SistemaM7PU");
            } catch (Exception e) {
                throw e;
            }
        }
        logger.info("JpaUtil.getEntityManager antes de emf.createEntityManager() ");
        return emf.createEntityManager();

    }

    public <T> T procurarObjeto(Class<T> classe, Object chave) {
        T obj = emf.createEntityManager().find(classe, chave);
        return obj;
    }

    public static Connection getConexao() throws Exception {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/m7?useTimeZone=true&serverTimezone=UTC&autoReconnect=true&useSSL=false";
        String login = "admin";
        String senha = "m71215";
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, login, senha);
            return con;
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            throw new Exception("Erro abrindo conexão: " + e.getMessage());
        }
        // return null;
    }
}
