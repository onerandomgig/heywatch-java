# Client Library and CLI for encoding Videos with HeyWatch

HeyWatch is a Video Encoding Web Service.


For more information:

* HeyWatch website: http://www.heywatchencoding.com
* API documentation: http://www.heywatchencoding.com/documentation
* Code generator: http://www.heywatchencoding.com/code-generator
* Contact: [support@heywatch.com](mailto:support@heywatch.com)
* Twitter: [@heywatch](http://twitter.com/heywatch) / [@sadikzzz](http://twitter.com/sadikzzz)

## Install

The latest artifacts are published to maven central. 

To add HeyWatch into your project, simply add the following to your pom.xml:

```xml
  <dependencies>
    <dependency>
      <groupId>com.heywatch</groupId>
      <artifactId>heywatch</artifactId>
      <version>0.1.0</version>
    </dependency>
  </dependencies>
```

Now, run maven:

```mvn clean install```

## Authentication

``` java
import com.heywatch.api.HeyWatchClient;
import com.heywatch.api.HeyWatchParameters;
import com.heywatch.api.HeyWatchException;
import com.heywatch.api.HeyWatchObject;

// login with your HeyWatch API key
HeyWatchClient hw = new HeyWatchClient("k-e4e96eaf314655cb31c78294b2b2bfe7");
```

## Usage

### Transferring a video to HeyWatch

``` java
HeyWatchParameters params = new HeyWatchParameters();
params.put("url", "http://site.com/yourvideo.mp4");
params.put("title", "filename");

HeyWatchObject download = hw.create("download", params);
```

An `HeyWatchObject` is just a `JSONObject`. So to access an attribute:

``` java
download.get("id");
```

A typical `HeyWatchObject` looks like this:

``` json
{
  "created_at": "2011-06-15T19:00:11+02:00",
  "error_msg": null,
  "title": "filename",
  "video_id": 0,
  "updated_at": "2011-06-15T19:00:11+02:00",
  "url": "http://site.com/yourvideo.mp4",
  "progress": {
    "current_length": 0,
    "speed": 0,
    "percent": 0,
    "time_left": "??"
  },
  "id": 4950011,
  "error_code": null,
  "length": 0,
  "status": "pending"
}
```

### Creating a job

``` java
HeyWatchParameters params = new HeyWatchParameters();
params.put("video_id", "9662090");
params.put("format_id", "mp4");
params.put("keep_video_size", "true");
params.put("ping_url", "http://yoursite.com/ping/heywatch?postid=123434");
params.put("output_url", "s3://accesskey:secretkey@myvideobucket/mp4/123434.mp4");

hw.create("job", params);
```

``` json
{
  "ping_url": "http://yoursite.com/ping/heywatch?postid=123434",
  "error_msg": null,
  "created_at": "2011-06-15T12:13:13+02:00",
  "video_id": 9662090,
  "updated_at": "2011-06-15T12:13:13+02:00",
  "progress": 0,
  "output_url": "s3://accesskey:secretkey@myvideobucket/mp4/123434.mp4",
  "format_id": "mp4",
  "id": 4944088,
  "error_code": null,
  "encoded_video_id": 0,
  "encoding_options": {
    "keep_video_size": true
  },
  "status": "pending",
}
```

### Generating thumbnails

``` java
HeyWatchParameters params = new HeyWatchParameters();
params.put("media_id", "9662142");
params.put("number", "6");
params.put("ping_url", "http://yoursite.com/ping/heywatch/thumbs");
params.put("output_url", "s3://accesskey:secretkey@bucket");

hw.create("preview/thumbnails", params);
```

### Using the Robot API

Note you can use the [code generator](http://www.heywatchencoding.com/code-generator) to write the INI file.

Once you have it:

``` java
HeyWatchObject job = hw.createRobotJob("heywatch.ini");
```

### Errors

If an error occurs, an `HeyWatchException` is raised.

Released under the [Apache license](http://www.apache.org/licenses/LICENSE-2.0.txt).
