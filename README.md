# Java client Library for encoding Videos with HeyWatch

## Install

Get the code on [Github](http://github.com/heywatch/heywatch-java)

## Submitting the job

Use the [API Request Builder](https://app.heywatch.com/job/new) to generate a config file that match your specific workflow.

Example of `heywatch.conf`:

``` language-hw
var s3 = s3://accesskey:secretkey@mybucket

set source  = http://yoursite.com/media/video.mp4
set webhook = http://mysite.com/webhook/heywatch

-> mp4  = $s3/videos/video.mp4
-> webm = $s3/videos/video.webm
-> jpg_300x = $s3/previews/thumbs_#num#.jpg, number=3
```

Here is the java code to submit the config file:

``` language-java
import com.heywatch.api.HeyWatchAPI;
import com.heywatch.api.HeyWatchException;
import com.heywatch.api.HeyWatchObject;

public class App {
  public static void main(String[] args) throws HeyWatchException {
    try {
      String config = "set source = https://s3-eu-west-1.amazonaws.com/media.heywatch.com/test.mp4\n"
          + "-> mp4 = s3://a:s@bucket/video.mp4\n";

      HeyWatchObject job = HeyWatchAPI.submit(config, "api-key");
      System.out.println("Job: " + job.get("id"));

    } catch(HeyWatchException e) {
      System.out.println("Error " + e.getMessage());
    }
  }
}
```

Note that you can use the environment variable `HEYWATCH_API_KEY` to set your API key.

*Released under the [Apache license](http://www.apache.org/licenses/LICENSE-2.0.txt).*

---

* HeyWatch website: http://www.heywatchencoding.com
* API documentation: http://www.heywatchencoding.com/docs
* Github: http://github.com/heywatch/heywatch_api-ruby
* Contact: [support@heywatch.com](mailto:support@heywatch.com)
* Twitter: [@heywatch](http://twitter.com/heywatch) / [@sadikzzz](http://twitter.com/sadikzzz)
