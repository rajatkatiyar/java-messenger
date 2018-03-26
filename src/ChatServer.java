import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;


public class ChatServer {
	static Vector ClientSockets;
	static Vector Loginnames;
	ChatServer() throws IOException{
		ServerSocket server=new ServerSocket(5217);
		ClientSockets=new Vector();
		 Loginnames=new Vector();
		 while(true){
			 Socket client=server.accept();
			 AcceptClient acceptClient=new AcceptClient(client);
		 }
	}

public static void main(String[] arrg) throws IOException{
	ChatServer client=new ChatServer();
}
 
class AcceptClient extends Thread{
	Socket ClientSocket;
	DataInputStream din;
	DataOutputStream dout;
	AcceptClient(Socket Client) throws IOException{
		ClientSocket =Client;
		din=new DataInputStream(ClientSocket.getInputStream());
		dout=new DataOutputStream(ClientSocket.getOutputStream());
		String Loginname=din.readUTF();
		Loginnames.add(ClientSocket);
		ClientSockets.add(ClientSocket);
		start();
		
		
	}
	public void run(){
		while(true){
			try{
				String msgFromClient =din.readUTF();
				StringTokenizer st=new StringTokenizer(msgFromClient);
				String LoginName=st.nextToken();
				String MsgType=st.nextToken();
				String msg="";
				int lo=-1; 
				while(st.hasMoreTokens()){
					msg=msg+ " " +st.nextToken(); 
				}
				if(MsgType.equals("LOGIN")){
				for(int i=0;i<Loginnames.size();i++){
					Socket pSocket=(Socket)ClientSockets.elementAt(i);
					DataOutputStream pout=new DataOutputStream(pSocket.getOutputStream());
					pout.writeUTF(LoginName+"has logged in.");
					
				}
				}
				else if(MsgType.equals("LOGOUT")){
					for(int i=0;i<Loginnames.size();i++){
						if(Loginnames.equals(Loginnames.elementAt(i)))
							lo=i;
						Socket pSocket=(Socket)ClientSockets.elementAt(i);
						DataOutputStream pout=new DataOutputStream(pSocket.getOutputStream());
						pout.writeUTF(LoginName+"has logged out ");	
					}
				
					if(lo >= 0){
						Loginnames.removeElementAt(lo);
						ClientSockets.removeElementAt(lo);
					}
				}
				else{
					for(int i=0;i<Loginnames.size();i++){
						Socket pSocket=(Socket)ClientSockets.elementAt(i);
						DataOutputStream pout=new DataOutputStream(pSocket.getOutputStream());
						pout.writeUTF(LoginName+" :"+msg);	
					}
				}
				if(MsgType.equals("LOGOUT"))
					break;
			}
			catch(IOException e){
				
			}
		}
	}

}

}