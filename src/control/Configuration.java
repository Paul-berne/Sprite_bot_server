package control;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class Configuration {
    
    // Propriétés pour stocker les données de configuration
    private Properties properties;
    
    // Encrypteur pour décrypter les données sensibles
    private StandardPBEStringEncryptor encryptor;

    // Constructeur
    public Configuration() {
        // Initialise les propriétés et l'encrypteur
        this.properties = new Properties();
        this.encryptor = new StandardPBEStringEncryptor();
        
        // Définit le mot de passe pour l'encrypteur
        this.encryptor.setPassword("P@ssw0rdsio");

        try (FileInputStream input = new FileInputStream("./data/vtg.cfg")) {
            // Charge les propriétés de configuration à partir du fichier
            properties.load(input);
        } catch (IOException e) {
            // Gère une éventuelle IOException si le chargement du fichier échoue
            e.printStackTrace();
        }
    }

    // Méthode pour lire une propriété et la décrypter
    public String readProperty(String theKey) {
        // Décrypte la valeur de la propriété en utilisant l'encrypteur
     	return encryptor.decrypt(properties.getProperty(theKey));
    }
}
