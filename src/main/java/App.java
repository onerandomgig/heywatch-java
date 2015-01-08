import com.heywatch.api.HeyWatchAPI;
import com.heywatch.api.HeyWatchException;
import com.heywatch.api.HeyWatchObject;

public class App {

	public static void main(String[] args) throws HeyWatchException {
		try {
			String config = "set source = https://s3-eu-west-1.amazonaws.com/media.heywatch.com/test.mp4\n"
					+ "-> mp4 = s3://a:s@bucket/video.mp4\n";

			HeyWatchObject job = HeyWatchAPI.submit(config);
			System.out.println("Job: " + job.get("id"));

		} catch(HeyWatchException e) {
			System.out.println("Error " + e.getMessage());
		}
	}

}
