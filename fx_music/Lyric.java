package fx_music;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;

import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * 用来封装歌词的类
 * @author 空太
 *
 */
public class Lyric
{ 
	private double y;
	private boolean flag;
	private boolean start_flag;
	private String data_time;
	
	private Media media;
	private LinkedHashMap<Double, String> map;
	private VBox vbox;
	
	private ArrayList<SimpleDoubleProperty> list;
	
	/**
	 * 播放歌词的类
	 * 参数 歌曲资源
	 */
	public Lyric(Media media)
	{
		this.media = media;
	}
	
	/**
	 * 刷新数据
	 * @param media
	 * @throws IOException
	 */
	public void refresh(Media media) throws IOException
	{
		this.media = null;
		this.media = media;
		 
		flag = init();
		start_flag = true;
		
		vbox.getChildren().remove(0, vbox.getChildren().size());
		list.clear();

		if(flag)
		{
			map.keySet().forEach(new Consumer<Double>()
			{
				@Override
				public void accept(Double t)
				{
					Label label= new Label(map.get(t)); 
					label.setFont(Font.font("Candara Light", 20));
					label.setWrapText(true);
					vbox.getChildren().add(label);
					
					label = null;
				}
			});
		}
		else
		{
			Label label = new Label("出现未知错误");
			label.setFont(Font.font("Candara Light", 20));
			label.setTextFill(Paint.valueOf("#FF0000"));
			vbox.getChildren().add(label);

			label = null;
		}
		vbox.setOpacity(0);
	}
	
	/**
	 * 返回歌词面板
	 * @return
	 * @throws IOException
	 */
	public SubScene createSubScene() throws IOException
	{	
		start_flag = false;
		list = new ArrayList<SimpleDoubleProperty>();
		map = new LinkedHashMap<Double, String>();
		
		vbox = new VBox(40);
		vbox.setStyle("-fx-background-color:#FFFFFF00");
		vbox.setAlignment(Pos.CENTER); 
		
		flag = init();
		
		if(flag)
		{
			map.keySet().forEach(new Consumer<Double>()
			{
				@Override
				public void accept(Double t)
				{
					Label label= new Label(map.get(t)); 
					label.setFont(Font.font("Candara Light", 20));
					label.setWrapText(true);
					vbox.getChildren().add(label);
					
					label = null;
				}
			});
		}
		else
		{
			Label label = new Label("出现未知错误");
			label.setFont(Font.font("Candara Light", 20));
			label.setTextFill(Paint.valueOf("#FF0000"));
			vbox.getChildren().add(label);

			label = null;
		}
		
		SubScene subscene = new SubScene(vbox, 500, 400, true, SceneAntialiasing.BALANCED);
		PerspectiveCamera camera = new PerspectiveCamera();
		
//		camera.setTranslateZ(10);
//		camera.setTranslateX(220);
//		camera.setTranslateY(200);
//		
//		camera.setFieldOfView(130);
		
		subscene.setCamera(camera);

		return subscene;
		
	}
	
	/**
	 * 初始化歌词位置
	 * 其中内部也调用
	 */
	public void start()
	{	
		y = 0 - vbox.getChildren().get(0).getLayoutY() + 200 + 65;
		
		vbox.getChildren().forEach(new Consumer<Node>()
		{	
			@Override
			public void accept(Node t)
			{
				t.setTranslateY(y);
				list.add(new SimpleDoubleProperty(y));
				
				t.boundsInParentProperty().addListener(new ChangeListener<Bounds>()
				{
					@Override
					public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue)
					{
						if(newValue != null)
						{
							Point2D p = t.localToParent(newValue.getMinX(), newValue.getMinY());
							if(p.getY() >= 400 && p.getY() <= 425)
							{
								Label label = (Label) t;
								label.setTextFill(Paint.valueOf("#ff0000"));
								label.setTranslateZ(0);
								
								label = null;
							}
							else
							{
								Label label = (Label) t;
								label.setTextFill(Paint.valueOf("#000000"));
								label.setTranslateZ(0);
								
								label = null;
							}
						}
					}
				});
			}
		});
	}
	
	/**
	 * 刷新是时调用
	 * 初始化歌词位置
	 */
	protected void start_flag()
	{
		y = 0 - vbox.getChildren().get(0).getLayoutY() + 200 + 65;
		
		vbox.getChildren().forEach(new Consumer<Node>()
		{	
			@Override
			public void accept(Node t)
			{
				t.setTranslateY(y);
				list.add(new SimpleDoubleProperty(y));
				
				t.boundsInParentProperty().addListener(new ChangeListener<Bounds>()
				{
					@Override
					public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue)
					{
						if(newValue != null)
						{
							Point2D p = t.localToParent(newValue.getMinX(), newValue.getMinY());
							if(p.getY() >= 400 && p.getY() <= 425)
							{
								Label label = (Label) t;
								label.setTextFill(Paint.valueOf("#ff0000"));
								label.setTranslateZ(0);
								
								label = null;
							}
							else
							{
								Label label = (Label) t;
								label.setTextFill(Paint.valueOf("#000000"));
								label.setTranslateZ(0);
								
								label = null;
							}
						}
					}
				});
			}
		});
	}
	
	/**
	 * 内部调用
	 * 初始化歌词文件
	 * @return
	 * @throws IOException
	 */
	protected Boolean init() throws IOException
	{
		if(media != null)
		{
			StringBuffer text = new StringBuffer();
			
			URL url = new URL(media.getSource());
			String name = URLDecoder.decode(url.getFile(), "UTF-8");
			File file = new File(name);
			
			String lyric_path = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 3) + "lrc";
			File lyric_file = new File(lyric_path);
			if(lyric_file.exists())
			{
				String code = getCode(lyric_file);
				
				InputStreamReader reader = new InputStreamReader(new FileInputStream(lyric_file), Charset.forName(code));
				
				while(reader.ready())
				{
					text.append((char) reader.read());
				}
				
				reader.close();
				
				map.clear();
				
				String[] lyric_text = text.toString().split("\n");
				String pattern = "\\[(.*?)\\]";
				String pattern1 = "\\](.*)";
				Pattern r = Pattern.compile(pattern);
				Pattern r1 = Pattern.compile(pattern1);
				for(String s : lyric_text)
				{
					Matcher m = r.matcher(s);
					Matcher m1 = r1.matcher(s);
					if(m.find() && m1.find())
					{
						if(m1.group(1).equals("") == false)
						{
							String[] times = m.group(1).split(":");
							String second = String.format("%.1f",Double.parseDouble(times[1]));
							double time = Double.parseDouble(times[0]) * 60+Double.parseDouble(second);
				
							map.put(time, m1.group(1));
						}
					}
					
					m = null;
					m1= null;
				}
				r = null;
				r1 = null;
				
				return true;
			}
			else
			{
				map.put(0.0,"暂无歌词文件");
				return true;
			}
		}
		else
			return false;
	}
	
	/**
	 * 内部调用
	 * 获得歌词文件的编码方式
	 * @param file
	 * @return
	 * @throws IOException
	 */
	protected String getCode(File file) throws IOException
	{
		String code = null;
		
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
		int p = (bin.read() << 8) + bin.read();
		switch (p) {
        case 0x5b30:
            code = "UTF-8";
            break;
//       case 0x5b74:
//        	code = "GBK|GB2312|ANSI";
//          	break;
        default:
            code = "GBK";
        }
		
		bin.close();
		
		return code;
	}
	
	/**
	 * 歌词播放
	 * 今后要改
	 * 现在不能运用所有情况
	 * @param time
	 */
	public void play_start(double time)
	{	
		if(start_flag)
		{
			start_flag();
			vbox.setOpacity(1);
			start_flag = false;
		}
		
		if(flag)
		{
			for(Double t : map.keySet())
			{
				if(t - 0.5 <=0)
				{
					data_time = String.format("%.1f", t);
				}
				else
					data_time = String.format("%.1f", t - 0.5);
				
				if(time == Double.valueOf(data_time))
				{
					play();
					break;
				}
			}
		}
	}
	
	/**
	 * 内部调用
	 * 歌词播放的封装方法
	 */
	protected void play()
	{	
		vbox.getChildren().forEach(new Consumer<Node>()
		{
			int i=0;
			@Override
			public void accept(Node t)
			{
				TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5));
				tt.setNode(t);
				
				tt.setFromY(0 + list.get(i).get());
				tt.setByY(-65);
				
				list.get(i).set(list.get(i).get() - 65);
				i++;
				
				tt.play();
				
				tt = null;
			}
		});
	}
	
	/**
	 * 播放完后的歌词重新加载
	 */
	public void reset()
	{
		vbox.getChildren().remove(0, vbox.getChildren().size());
		list.clear();
		
		map.keySet().forEach(new Consumer<Double>()
		{
			@Override
			public void accept(Double t)
			{
				Label label= new Label(map.get(t)); 
				label.setFont(Font.font("Candara Light", 20));
				label.setWrapText(true);
				vbox.getChildren().add(label);
				
				label = null;
			}
		});
		
		vbox.getChildren().forEach(new Consumer<Node>()
		{
			@Override
			public void accept(Node t)
			{
				t.setTranslateY(y);
				list.add(new SimpleDoubleProperty(y));
				
				t.boundsInParentProperty().addListener(new ChangeListener<Bounds>()
				{
					@Override
					public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue)
					{
						if(newValue != null)
						{
							Point2D p = t.localToParent(newValue.getMinX(), newValue.getMinY());
							if(p.getY() >= 400 && p.getY() <= 425)
							{
								Label label = (Label) t;
								label.setTextFill(Paint.valueOf("#ff0000"));
								label.setTranslateZ(0);
								
								label = null;
							}
							else
							{
								Label label = (Label) t;
								label.setTextFill(Paint.valueOf("#000000"));
								label.setTranslateZ(0);
								
								label = null;
							}
						}
					}
				});
			}
		});
	}
}
