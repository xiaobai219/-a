package fx_music;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

/**
 * @author 空太
 *会生成一个进度条
 *其实就是生成一个AnchorPane
 *上面放了矩形 来演示进度条
 */
public class Progress
{
	private double iv_leight = 350;
	
	private Media media;
	private ImageView iv;
	private double height;
	private double width;
	private Paint paint;
	private MediaPlayer mediaplayer;
	private Timeline timeline;
	private Rectangle rec2;
	private Scale scale;
	private AnchorPane an;
	private SimpleStringProperty totaltime1;
	
	private URL url;
	private ImageView play_iv;
	
	private Label title;
	private Label artist;
	private Label album;
	
	/**
	 * 参数： 
	 * medai 加载的音频
	 * iv 音频自带的图片
	 * width 进度条的宽度
	 * height 进度条的高度
	 * Paint 进度条的颜色
	 * mediaplayer 装载音频
	 * url 播放的图片路径
	 * play_iv 装载播放暂停的图片
	 * totaltime 音乐的总时间
	 * title 音乐名
	 * artist 作者
	 * album 专辑
	 */
	public Progress(Media media, MediaPlayer mediaPlayer, ImageView iv,double width, double height, Paint paint, URL url, ImageView play_iv, SimpleStringProperty totaltime,Label title, Label artist, Label album)
	{
		this.media = media;
		this.mediaplayer = mediaPlayer;
		this.iv = iv;
		this.height = height;
		this.width = width;
		this.paint = paint;
		this.url = url;
		this.play_iv = play_iv;
		this.totaltime1 = totaltime;
		this.title = title;
		this.artist = artist;
		this.album = album;
	}
	
	/**
	 * 重新刷新对象
	 * @param media
	 * @param mediaplayer
	 * @return
	 */
	public void refresh(Media media,  MediaPlayer mediaplayer)
	{
		this.media = null;
		this.media = media;
		if(this.mediaplayer != null)
		{
			this.mediaplayer.dispose();
		}
		this.mediaplayer = mediaplayer;
		
		init2();
		
		this.rec2.setOpacity(1);
	}
	
	/**
	 * 创建一个AnchorPane
	 * 里面就是两个矩形形成的进度条
	 */
	public AnchorPane createProgress()
	{
		an = new AnchorPane();
		
		Rectangle rec1 = new Rectangle(width, height);
		rec2 = new Rectangle(1, height);
		
		rec1.setFill(Color.TRANSPARENT);
		rec1.setStroke(Paint.valueOf("#999999"));
		rec1.setStrokeWidth(1);
		rec1.setArcHeight(2);
		rec1.setArcWidth(2);
		
		rec2.setFill(paint);
		
		scale = new Scale(1, 1, 0, 0);
		rec2.getTransforms().add(scale);
		
		an.getChildren().addAll(rec1,rec2);
		
		timeline = new Timeline();
		
		init();
		
		rec1 = null;
		
		return an;
		
	}
	/**
	 * 这里方法是初始化一些参数
	 * 此方法在createProgress()方法中自动调用
	 * 这里不可调用
	 * 否则会出错
	 */
	protected void init()
	{
		rec2.setOpacity(0);
		if(mediaplayer != null)
		{
			mediaplayer.setOnReady(new Runnable()
			{
				@Override
				public void run()
				{
					if(media.getMetadata().get("image") == null)
					{
						URL url = this.getClass().getClassLoader().getResource("img/VOCALOID.jpg");
						iv.setImage(new Image(url.toExternalForm()));
						
						url = null;
					}
					else
					{
						iv.setImage((Image) media.getMetadata().get("image"));
					}
					
					if(iv.prefHeight(-1) > iv.prefWidth(-1))
					{
						iv.setFitWidth(iv_leight);
					}
					else
					{
						iv.setFitHeight(iv_leight);
					}
					
					if(media.getMetadata().get("title") == null)
					{
						try
						{
							URL url = new URL(media.getSource());
							String title_name = URLDecoder.decode(url.getFile(), "UTF-8");
							File file = new File(title_name);
							String name = file.getName().substring(0, file.getName().length() -4);
							title.setText("歌曲名：" + name);
							
							url = null;
							title_name = null;
							file = null;
							name = null;
						} catch (MalformedURLException | UnsupportedEncodingException e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						title.setText("歌曲名：" + media.getMetadata().get("title").toString());
					}
					
					if(media.getMetadata().get("artist") == null)
					{
						artist.setText("作   者：未知");
					}
					else
					{
						artist.setText("作   者：" + media.getMetadata().get("artist").toString());
					}
					
					if(media.getMetadata().get("album") == null)
					{
						album.setText("专   辑：未知");
					}
					else
					{
						album.setText("专   辑：" + media.getMetadata().get("album").toString());
					}
					
					timeline.stop();
					timeline.getKeyFrames().clear();
					
					totaltime1.set(String.format("%.2f", mediaplayer.getTotalDuration().toMinutes()));
					
					KeyValue kv1Y = new KeyValue(scale.xProperty(), 0);
					KeyFrame kf1 = new KeyFrame(Duration.seconds(0), kv1Y);
					
					KeyValue kv2Y = new KeyValue(scale.xProperty(), width);
					KeyFrame kf2 = new KeyFrame(mediaplayer.getTotalDuration(), kv2Y);
					
					timeline.getKeyFrames().addAll(kf1, kf2);
					
					kv1Y = null;
					kf1 = null;
					
					kv2Y = null;
					kf2 = null;
					
				}
			});
			
			an.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent event)
				{
					double x = event.getX();
					double pai = x / width;
					double time = pai * mediaplayer.getTotalDuration().toSeconds();
					
					double nexttime = mediaplayer.getTotalDuration().toSeconds() - time;
					
					timeline.stop();
					timeline.getKeyFrames().clear();
					
					Scale scale = (Scale)rec2.getTransforms().get(0);
					
					KeyValue kv1Y = new KeyValue(scale.xProperty(), x);
					KeyFrame kf1 = new KeyFrame(Duration.seconds(0), kv1Y);
					
					KeyValue kv2Y = new KeyValue(scale.xProperty(), width);
					KeyFrame kf2 = new KeyFrame(Duration.seconds(nexttime), kv2Y);
					
					timeline.getKeyFrames().addAll(kf1, kf2);
					timeline.play();
					
					if(mediaplayer.getStatus() != Status.PLAYING)
					{
						mediaplayer.play();
						rec2.setOpacity(1);
						Image image = new Image(url.toExternalForm());
						play_iv.setImage(image);
						
						image = null;
					}
					
					mediaplayer.seek(Duration.seconds(time));	
					
					scale = null;
					
					kv1Y = null;
					kf1 = null;
					
					kv2Y = null;
					kf2 = null;
				}
			});
			
			timeline.setOnFinished(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent event)
				{
					timeline.getKeyFrames().clear();
					
					KeyValue kv1Y = new KeyValue(scale.xProperty(), 0);
					KeyFrame kf1 = new KeyFrame(Duration.seconds(0), kv1Y);
					
					KeyValue kv2Y = new KeyValue(scale.xProperty(), width);
					KeyFrame kf2 = new KeyFrame(mediaplayer.getTotalDuration(), kv2Y);
					
					timeline.getKeyFrames().addAll(kf1, kf2);
					
					timeline.play();
					
				}
			});
		}
		else
		{
			Setheader_iv(iv);
		}
	}
	/**
	 * 这里方法是初始化一些参数
	 * 此方法在refresh()方法中自动调用
	 * 这里不可调用
	 * 否则会出错
	 */
	protected void init2()
	{
		rec2.setOpacity(0);

		mediaplayer.setOnReady(new Runnable()
		{
			@Override
			public void run()
			{
				if(media.getMetadata().get("image") == null)
				{
					URL url = this.getClass().getClassLoader().getResource("img/VOCALOID.jpg");
					iv.setImage(new Image(url.toExternalForm()));
					
					url = null;
				}
				else
				{
					iv.setImage((Image) media.getMetadata().get("image"));
				}
				
				if(iv.prefHeight(-1) > iv.prefWidth(-1))
				{
					iv.setFitWidth(iv_leight);
				}
				else
				{
					iv.setFitHeight(iv_leight);
				}
				
				if(media.getMetadata().get("title") == null)
				{
					try
					{
						URL url = new URL(media.getSource());
						String title_name = URLDecoder.decode(url.getFile(), "UTF-8");
						File file = new File(title_name);
						String name = file.getName().substring(0, file.getName().length() -4);
						title.setText("歌曲名：" + name);
						
						url = null;
						title_name = null;
						file = null;
						name = null;
					} catch (MalformedURLException | UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					title.setText("歌曲名：" + media.getMetadata().get("title").toString());
				}
				
				if(media.getMetadata().get("artist") == null)
				{
					artist.setText("作   者：未知");
				}
				else
				{
					artist.setText("作   者：" + media.getMetadata().get("artist").toString());
				}
				
				if(media.getMetadata().get("album") == null)
				{
					album.setText("专   辑：未知");
				}
				else
				{
					album.setText("专   辑：" + media.getMetadata().get("album").toString());
				}
				
				timeline.stop();
				timeline.getKeyFrames().clear();
				
				totaltime1.set(String.format("%.2f", mediaplayer.getTotalDuration().toMinutes()));
				
				KeyValue kv1Y = new KeyValue(scale.xProperty(), 0);
				KeyFrame kf1 = new KeyFrame(Duration.seconds(0), kv1Y);
				
				KeyValue kv2Y = new KeyValue(scale.xProperty(), width);
				KeyFrame kf2 = new KeyFrame(mediaplayer.getTotalDuration(), kv2Y);
				
				timeline.getKeyFrames().addAll(kf1, kf2);
				timeline.play();
				
				kv1Y = null;
				kf1 = null;
				
				kv2Y = null;
				kf2 = null;
				
			}
		});
		
		an.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				double x = event.getX();
				double pai = x / width;
				double time = pai * mediaplayer.getTotalDuration().toSeconds();
				
				double nexttime = mediaplayer.getTotalDuration().toSeconds() - time;
				
				timeline.stop();
				timeline.getKeyFrames().clear();
				
				Scale scale = (Scale)rec2.getTransforms().get(0);
				
				KeyValue kv1Y = new KeyValue(scale.xProperty(), x);
				KeyFrame kf1 = new KeyFrame(Duration.seconds(0), kv1Y);
				
				KeyValue kv2Y = new KeyValue(scale.xProperty(), width);
				KeyFrame kf2 = new KeyFrame(Duration.seconds(nexttime), kv2Y);
				
				timeline.getKeyFrames().addAll(kf1, kf2);
				timeline.play();
				
				if(mediaplayer.getStatus() != Status.PLAYING)
				{
					mediaplayer.play();
					
					rec2.setOpacity(1);
					Image image = new Image(url.toExternalForm());
					play_iv.setImage(image);
					
					image = null;
				}
				
				mediaplayer.seek(Duration.seconds(time));	
				
				scale = null;
				
				kv1Y = null;
				kf1 = null; 
				
				kv2Y = null;
				kf2 = null;
			}
		});
		
		timeline.setOnFinished(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				timeline.getKeyFrames().clear();
				
				KeyValue kv1Y = new KeyValue(scale.xProperty(), 0);
				KeyFrame kf1 = new KeyFrame(Duration.seconds(0), kv1Y);
				
				KeyValue kv2Y = new KeyValue(scale.xProperty(), width);
				KeyFrame kf2 = new KeyFrame(mediaplayer.getTotalDuration(), kv2Y);
				
				timeline.getKeyFrames().addAll(kf1, kf2);
				
				timeline.play();
				
			}
		});
	}
	/**
	 * 初始化 图片
	 * @param iv
	 */
	protected void Setheader_iv(ImageView iv)
	{
		URL url = this.getClass().getClassLoader().getResource("img/VOCALOID.jpg");
		iv.setImage(new Image(url.toExternalForm()));
		if(iv.prefHeight(-1) > iv.prefWidth(-1))
		{
			iv.setFitWidth(200.0);
		}
		else
		{
			iv.setFitHeight(200.0);
		}
	}
	
	
	/**
	 * 进度条播放
	 */
	public void play()
	{
		timeline.play();
		rec2.setOpacity(1);
	}
	
	/**
	 * 进度条暂停
	 */
	public void pause() 
	{
		timeline.pause();
	}
}