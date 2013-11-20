import com.heywatch.api.HeyWatchClient;
import com.heywatch.api.HeyWatchException;
import com.heywatch.api.HeyWatchObject;
import com.heywatch.api.HeyWatchParameters;

public class App {

	public static void main(String[] args) throws HeyWatchException {
		HeyWatchClient hw = new HeyWatchClient("k-apikey");
		HeyWatchObject account = hw.account();
		System.out.println("account: " + account.get("login"));
		
		// Transferring a video
		HeyWatchParameters params = new HeyWatchParameters();
		params.put("url", "http://test.com/test.mp4");
		params.put("title", "abc1234");
		params.put("ping_url", "http://mysite.com/ping/heywatch?postid=abc1234");
			
		HeyWatchObject download = hw.create("download", params);
		System.out.println(download);
		
		// Creating a job
		params = new HeyWatchParameters();
		params.put("video_id", "ID");
		params.put("format_id", "ios_720p");
		params.put("ping_url", "http://mysite.com/ping/heywatch?postid=abc1234");
		params.put("output_url", "s3://accesskey:secretkey@bucket/video/abc2134.mp4");
			
		HeyWatchObject job = hw.create("job", params);
		System.out.println(job);
		
		// Creating a Robot Job
		HeyWatchObject robotJob = hw.createRobotJob("path/to/heywatch.ini");
		System.out.println(robotJob);
		
		// Generating thumbnails
		params = new HeyWatchParameters();
		params.put("video_id", "ID");
		params.put("number", "6");
		params.put("filename", "abc1234_#num#");
		params.put("ping_url", "http://mysite.com/ping/heywatch?postid=abc1234");
		params.put("output_url", "s3://accesskey:secretkey@bucket/thumbnails");
			
		hw.create("preview/thumbnails", params);
	}

}
