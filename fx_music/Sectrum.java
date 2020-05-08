package fx_music;

import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

/**
 * 
 * @author 空太
 * 会生成频谱
 * 其实就是生成一个HBox
 * 上面有一排矩形
 *
 */
public class Sectrum
{
	private int HEIGHT;
	private int UNMBER;
	private int index = 0;
	
	private MediaPlayer mediaplayer;
	private ArrayList<Rectangle> list;
	private ArrayList<SimpleDoubleProperty> list_data;
	private ArrayList<Integer> data;
	
	public Sectrum(MediaPlayer mediaplayer)
	{
		this.mediaplayer = mediaplayer;
	} 
	
	/**
	 * 重新刷新对象 
	 * @param mediaplayer
	 * @return
	 */
	public void refresh(MediaPlayer mediaplayer)
	{
		if(this.mediaplayer != null)
		{
			this.mediaplayer.dispose();
		}
		this.mediaplayer = mediaplayer;
		
		addSpectrum();
		
		this.mediaplayer.play();
		
	}
	/**
	 * 创建HBox
	 * 参数分别是
	 * height 矩形能达到的最大高度
	 * number 矩形数量
	 * paint 矩形颜色
	 */
	public HBox createRectangle(int height,int number, Paint paint)
	{
		HBox hbox =new HBox(2);
		UNMBER = number;
		HEIGHT = height;
		
		list = new ArrayList<Rectangle>();
		list_data = new ArrayList<SimpleDoubleProperty>();
		data = new ArrayList<Integer>();
		
		for(int i=0; i<UNMBER; i++) 
		{
			Rectangle rec = new Rectangle(5, 5);
			rec.setFill(paint);
			list.add(rec);
			Scale scale = new Scale(1, 1, 0, 5);
			list.get(i).getTransforms().add(scale);
			hbox.getChildren().add(list.get(i));
			SimpleDoubleProperty value = new SimpleDoubleProperty(1.0);
			list_data.add(value);
			
			data.add(1);
			
			rec = null;
			scale = null;
			value = null;
			
		}
		
		addSpectrum();
		
		return hbox;
	}
	
	public void addSpectrum()
	{
		if(mediaplayer != null)
		{
			mediaplayer.setAudioSpectrumListener(new AudioSpectrumListener()
			{	
				@Override
				public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases)
				{
					index = 0;
					data.forEach(new Consumer<Integer>()
					{
						@Override
						public void accept(Integer t)
						{
							Start(getData(magnitudes[index]),(Scale)list.get(index).getTransforms().get(0),list_data.get(index));
							index++;
						}
					});
				}
			});
		}
	}
	
	/**
	 * 数据变换
	 */
	protected double getData(float magnitudes)
	{
		float f = Math.abs(magnitudes);
		
		double data = HEIGHT - (f * HEIGHT) / 60;
		
		return data;
		
	}
	
	protected void Start(double data,Scale scale,SimpleDoubleProperty list_data)
	{	
		double second_y = (data + 5) /5;
	
		KeyValue kv1Y = new KeyValue(scale.yProperty(), list_data.get());
		KeyFrame kf1 = new KeyFrame(Duration.seconds(0), kv1Y);
		
		KeyValue kv2Y = new KeyValue(scale.yProperty(), second_y);
		KeyFrame kf2 = new KeyFrame(Duration.seconds(0.1), kv2Y);
		
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().addAll(kf1, kf2);
		
		list_data.set(second_y);
		
		timeline.play();
		
		kv1Y = null;
		kf1 = null;
		
		kv2Y = null;
		kf2 = null;
		
		timeline = null;
	}
	 
	/**
	 * 播放音乐
	 */
	public void play() 
	{
		mediaplayer.play();
	}
	
	/**
	 * 判断音乐是否正处于播放状态
	 */
	public boolean isPlaying()
	{
		return mediaplayer.getStatus() == Status.PLAYING ? true : false;
	}
	
	/**
	 * 暂停音乐
	 */
	public void pause()
	{
		mediaplayer.pause();
	}
}
