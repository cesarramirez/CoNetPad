

package org.ndacm.acmgroup.cnp.database;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.ndacm.acmgroup.cnp.Account;
import org.ndacm.acmgroup.cnp.CNPPrivateSession;
import org.ndacm.acmgroup.cnp.CNPServer;
import org.ndacm.acmgroup.cnp.CNPSession;
import org.ndacm.acmgroup.cnp.exceptions.FailedAccountException;
import org.ndacm.acmgroup.cnp.exceptions.FailedSessionException;

/**
 * Class:  Database<br>
 * Description:  This is a class for handling our database stuff.  
 * 
 */
public class Database implements IDatabase{

	private static final String DRIVER_CLASS = "org.sqlite.JDBC";
	private static final String ENCRYPTION_ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final String DB_FILE = "jdbc:sqlite:src//sqllite//CoNetPad.db3";

	private Connection dbConnection;
	private Random random;

	/**
	 * Default Constructor
	 * @throws SQLException 
	 * @throws Exception
	 */
	public Database() throws ClassNotFoundException, SQLException
	{
		Class.forName(DRIVER_CLASS);
		dbConnection = DriverManager.getConnection(DB_FILE);
		random = new Random();

	}
	/**
	 * createAccount()
	 * This Creates a new user account and returns an object
	 * @param username - String The string username you wish to use to create new account
	 * @param email - String The password of the new account
	 * @param password - String The RAW password to be given.  Encrpytion is done for you.
	 * @return Returns an new Account Object or throws an FailedAccountException
	 * @throws FailedAccountException 
	 */
	public Account createAccount(String username, String email, String password) throws FailedAccountException {

		Account newAccount = null;

		// salt and hash password
		// http://stackoverflow.com/questions/2860943/suggestions-for-library-to-hash-passwords-in-java
		// http://stackoverflow.com/questions/5499924/convert-java-string-to-byte-array
		String hashString = null, saltString = null;
		byte[] salt = new byte[16];
		random.nextBytes(salt);

		try {
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 2048, 160);
			SecretKeyFactory f = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
			hashString = new String(f.generateSecret(spec).getEncoded());
			saltString = new String(salt, "ISO-8859-1");

			// test if username/email already exists

			// insert user into DB
			PreparedStatement registerUser = null;
			String insertion = "INSERT INTO UserAccount (Username, AccountPassword, AccountSalt, Email) "
					+ "VALUES (? , ?, ?, ?)";

			registerUser = dbConnection.prepareStatement(insertion);
			registerUser.setString(1, username);
			registerUser.setString(2, hashString);
			registerUser.setString(3, saltString);
			registerUser.setString(4, email);

			registerUser.executeUpdate();

			// return the account that was just inserted
			newAccount = retrieveAccount(username, password);

			registerUser.close();

		} catch (NoSuchAlgorithmException ex) {
			System.err.println("Invalid Encrpytion Algorithm: " + ENCRYPTION_ALGORITHM);
			throw new FailedAccountException("Error creating account for " + username);
		} catch (InvalidKeySpecException e) {
			System.err.println("Invalid key spec.");
			throw new FailedAccountException("Error creating account for " + username);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Unsupported encoding.");
			throw new FailedAccountException("Error creating account for " + username);
		} catch (SQLException e) {
			System.err.println("SQL error.");
			throw new FailedAccountException("Error creating account for " + username);
		}
		catch(Exception e)
		{
			System.err.println("Unknown Exception thrown:  " + e.getStackTrace());
			throw new FailedAccountException("Some Exception Thrown.");
		}

		if (newAccount != null) {
			return newAccount;
		} else {
			throw new FailedAccountException("Error creating account for " + username);
		}

	}
	/**
	 * retrieveAccount()
	 * Gets an existing account from the database and returns it into an Account Object
	 * @param username The username you wish to try and get
	 * @param password The password you wish to verify with.  Make sure its RAW and not encrypted.
	 * @return Account The account object of the the user account
	 * @throws FailedAccountException
	 */
	public Account retrieveAccount(String username, String password) throws FailedAccountException {

		PreparedStatement retrieveAccount = null;
		ResultSet rset = null;
		Account accountRetrieved = null;
		String saltRetrieved = null;
		String hashRetrieved = null;

		String query = "SELECT * "
				+ "FROM UserAccount "
				+ "WHERE username = ?";

		try {
			// retrieve user with given username
			retrieveAccount = dbConnection.prepareStatement(query);
			retrieveAccount.setString(1, username);

			//run the query, return a result set        
			rset = retrieveAccount.executeQuery();

			int idRetrieved = rset.getInt("UserID");
			String nameRetrieved = rset.getString("UserName");
			String emailRetrieved = rset.getString("Email");
			accountRetrieved = new Account(nameRetrieved, emailRetrieved, idRetrieved);

			hashRetrieved = rset.getString("AccountPassword");
			saltRetrieved = rset.getString("AccountSalt");

			//clean up database classes
			retrieveAccount.close();
			rset.close();

		} catch (SQLException ex) {
			throw new FailedAccountException("Error retrieving account for " + username);
		}

		String hashSupplied = null;

		// generate hash for the password string supplied (using the salt from userRetrieved)
		try {
			byte[] salt = saltRetrieved.getBytes("ISO-8859-1");
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 2048, 160);
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			hashSupplied = new String(f.generateSecret(spec).getEncoded());
		} catch (NoSuchAlgorithmException ex) {
			System.err.println("Invalid Encrpytion Algorithm: " + ENCRYPTION_ALGORITHM);
			throw new FailedAccountException("Error retrieving account for " + username);
		} catch (UnsupportedEncodingException ex) {
			System.err.println("Unsupported encoding.");
			throw new FailedAccountException("Error retrieving account for " + username);
		} catch (InvalidKeySpecException ex) {
			System.err.println("Invalid key spec.");
			throw new FailedAccountException("Error retrieving account for " + username);
		}
		catch(NullPointerException e)
		{
				System.err.println("Some other Error was caught");
			throw new FailedAccountException("Error  " + e.getStackTrace() );
		}
		// check if hashes match. if so, return account.
		if (hashSupplied.equals(hashRetrieved)) {
			return accountRetrieved;
		} else {
			throw new FailedAccountException("Incorrect password supplied for " + username);
		}

	}


	public CNPSession createSession(int sessionLeader, CNPServer server) throws SQLException {

		// create session and store in database			
		CNPSession newSession = null;

		// TODO test if session already exists
		
		// insert session into DB
		PreparedStatement createSession = null, retrieveSession = null;
	
		String query = "SELECT * "
				+ "FROM UserAccount "
				+ "WHERE UserID = ?";
		
		retrieveSession = dbConnection.prepareStatement(query);
		retrieveSession.setInt(1, sessionLeader);
		ResultSet rset = retrieveSession.executeQuery();
		if(rset.next())
		{
			
			String sessionName = CNPSession.generateString();
			String insertion = "INSERT INTO Session (SessionLeader, SessionName, IsPublic) "
					+ "VALUES (? , ?, ?)";
	
			createSession = dbConnection.prepareStatement(insertion);
			createSession.setInt(1, sessionLeader);
			createSession.setString(2, sessionName);
			createSession.setBoolean(3, true);
	
			createSession.executeUpdate();
	
			// return the session that was just inserted
			newSession = retrieveSession(sessionName, server);
	
			createSession.close();
			return newSession;
		}
		else
		{
			System.err.println("The SessionLeader was not found.");
			throw new FailedAccountException("Could not find Session Leader.");
		}
	

	}

	public CNPPrivateSession createSession(int sessionLeader, CNPServer server, String sessionPassword) 
			throws SQLException, FailedSessionException {

		// create session and store in database
		CNPPrivateSession newSession = null;

		// TODO test if session already exists

		// salt and hash password
		// http://stackoverflow.com/questions/2860943/suggestions-for-library-to-hash-passwords-in-java
		// http://stackoverflow.com/questions/5499924/convert-java-string-to-byte-array
		String hashString = null, saltString = null;
		byte[] salt = new byte[16];
		random.nextBytes(salt);

		try {
			KeySpec spec = new PBEKeySpec(sessionPassword.toCharArray(), salt, 2048, 160);
			SecretKeyFactory f = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
			hashString = new String(f.generateSecret(spec).getEncoded());
			saltString = new String(salt, "ISO-8859-1");

			// insert session into DB
			PreparedStatement createSession = null;
			String sessionName = CNPSession.generateString();
			String insertion = "INSERT INTO Session (SessionLeader, SessionName, IsPublic, SessionPassword, SessionSalt) "
					+ "VALUES (? , ?, ?, ?, ?)";

			createSession = dbConnection.prepareStatement(insertion);
			createSession.setInt(1, sessionLeader);
			createSession.setString(2, sessionName);
			createSession.setBoolean(3, false);
			createSession.setString(4, hashString);
			createSession.setString(5, saltString);

			createSession.executeUpdate();

			// return the account that was just inserted
			newSession = retrieveSession(sessionName, server, sessionPassword);

			createSession.close();

		} catch (NoSuchAlgorithmException ex) {
			System.err.println("Invalid Encrpytion Algorithm: " + ENCRYPTION_ALGORITHM);
			throw new FailedAccountException("Error creating session.");
		} catch (InvalidKeySpecException e) {
			System.err.println("Invalid key spec.");
			throw new FailedAccountException("Error creating session.");
		} catch (UnsupportedEncodingException e) {
			System.err.println("Unsupported encoding.");
			throw new FailedAccountException("Error creating session.");
		}
		catch(FailedSessionException e)
		{
			throw e;
		}

		return newSession;
	}

	public CNPSession retrieveSession(String sessionName, CNPServer server) throws SQLException {

		PreparedStatement retrieveSession = null;
		ResultSet rset = null;
		CNPSession sessionRetrieved = null;
		
		String query = "SELECT * "
				+ "FROM Session "
				+ "WHERE SessionName = ?";


		// retrieve user with given username
		retrieveSession = dbConnection.prepareStatement(query);
		retrieveSession.setString(1, sessionName);

		//run the query, return a result set        
		rset = retrieveSession.executeQuery();

		int idRetrieved = rset.getInt("SessionID");
		String nameRetrieved = rset.getString("SessionName");
		int sessionLeader = rset.getInt("SessionLeader");
		sessionRetrieved = new CNPSession(idRetrieved, nameRetrieved, server, sessionLeader);

		//clean up database classes
		retrieveSession.close();
		rset.close();

		return sessionRetrieved;
	}

	public CNPPrivateSession retrieveSession(String sessionName, CNPServer server, String sessionPassword) throws FailedSessionException, FailedAccountException 
	{
		PreparedStatement retrieveSession = null;
		ResultSet rset = null;

		String query = "SELECT * "
				+ "FROM Sesson "
				+ "WHERE SessionName = ?";

		try{
			// retrieve user with given username
			retrieveSession = dbConnection.prepareStatement(query);
			retrieveSession.setString(1, sessionName);
	
			//run the query, return a result set        
			rset = retrieveSession.executeQuery();
			if(rset.next())
			{
				int idRetrieved = rset.getInt("SessionID");
				String nameRetrieved = rset.getString("SessionName");
				int sessionLeader = rset.getInt("SessionLeader");
				String salt = rset.getString("AccountSalt");
				//Verify Password
				query = "SELECT * "
						+ "FROM SessonPassword "
						+ "WHERE SessionID = ?";
				retrieveSession = dbConnection.prepareStatement(query);
				retrieveSession.setInt(1, idRetrieved );
				//run the query, return a result set        
				rset = retrieveSession.executeQuery();
				if(rset.next())
				{
					retrieveSession.close();
					rset.close();
					String sessionPassword2 = rset.getString("SessionPassword");
					String sessionPaswordHash = this.encrypt(sessionPassword, salt);
					if(sessionPassword2.equals(sessionPaswordHash))
					{
						return new CNPPrivateSession(idRetrieved, nameRetrieved, server, sessionLeader);
					}
					else
					{
						System.err.println("Passwords Did not Match.");
						throw new FailedSessionException("Passwords did not match");
					}
					
				}
				else
				{
					System.err.println("No SessionPassword Found");
					throw new FailedSessionException("Session Password was given, but no correspodning session password found.");
				}	
			}
			else
			{
				System.err.println("No Session Found");
				throw new FailedSessionException("No Sesison was found");
			}
		}
		catch (NoSuchAlgorithmException ex) 
		{
			System.err.println("Invalid Encrpytion Algorithm: " + ENCRYPTION_ALGORITHM);
			throw new FailedAccountException("Error creating session.");
		} 
		catch (InvalidKeySpecException e) 
		{
			System.err.println("Invalid key spec.");
			throw new FailedAccountException("Error creating session.");
		} 
		catch(SQLException e)
		{
			System.err.println("SQL Error");
			throw new FailedSessionException("SQL Error.");
		}

	}

	public boolean sessionIsPrivate(String sessionName) {
		// TODO implement
		return false;
	}

	public boolean createSessionAccount(CNPSession session, Account account,
			Account.FilePermissionLevel filePermission, Account.ChatPermissionLevel chatPermission) {
		// TODO implement
		return false;
	}
	private String encrypt(String input, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException 
	{
		
		KeySpec spec = new PBEKeySpec(input.toCharArray(), salt.getBytes(), 2048, 160);
		SecretKeyFactory f = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
		return new String(f.generateSecret(spec).getEncoded());
	}
	

}


