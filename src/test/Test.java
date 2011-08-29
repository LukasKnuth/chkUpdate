package test;

import java.util.Date;

import org.knuth.chkupdate.Provider;
import org.knuth.chkupdate.UpdateCheck;
import org.knuth.chkupdate.UpdateResult;
import org.knuth.chkupdate.Version;
import org.knuth.chkupdate.exceptions.UpdateRequestException;
import org.knuth.chkupdate.exceptions.UpdateResponseException;
import org.knuth.chkupdate.provider.GitHubProvider;
import org.knuth.chkupdate.provider.SourceForgeProvider;

public class Test {
	
	public Test(){
		System.out.println("Check GitHub");
		github();
		System.out.println("-----\nCheck SourceForge");
		sourceforge();
	}
	
	private void github(){
		Version v = new Version("v0.1");
		GitHubProvider gh = new GitHubProvider("LukasKnuth", "NoteBook");
		UpdateCheck check = new UpdateCheck(gh);
		try {
			UpdateResult res = check.checkForUpdates();
			System.out.println("Lokal: "+v.toString()+"|Remote: "+res.getVersion().toString());
			System.out.println("Braucht Update: "+res.getVersion().isNewerThen(v));
		} catch (UpdateRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UpdateResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void sourceforge(){
		Date current = new Date((2007 - 1900), 12, 12);
		Provider sf = new SourceForgeProvider("ajaxmytop");
		UpdateCheck chk = new UpdateCheck(sf);
		try {
			UpdateResult res = chk.checkForUpdates();
			System.out.println("Lokal: "+current.toLocaleString()+
					"|Remote: "+res.getUpdateDate().toLocaleString());
			System.out.print("Braucht update: ");
			if (res.getUpdateDate().after(current)){
				System.out.print("true");
			} else System.out.print("false"); 
		} catch (UpdateRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UpdateResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		new Test();
	}

}
