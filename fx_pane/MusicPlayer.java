package fx_pane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import fx_music.Listview;
import fx_music.Lyric;
import fx_music.Progress;
import fx_music.Sectrum;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.ModifierValue;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * version 1.6
 * @author 空太
 * 2020年4月7号
 * 暂时放一段落
 */
public class MusicPlayer extends Application
{
	private int index;
	private double x;
	private double y;
	private double old_time;
	private double new_time;
	private double width = 1500;
	private double height = 800;
	private SimpleStringProperty totaltime = new SimpleStringProperty();
	
	private MediaPlayer mediaplayer;
	private Media media;
	
	private Sectrum sectrum;
	private Progress progress;
	private Lyric lyric;
	
	private ImageView close_iv;
	private ImageView zuida_iv;
	private ImageView zuixiao_iv;
	
	private ImageView method_iv;
	private ImageView play_iv;
	private ImageView playlist_iv;
	private ImageView toleft_iv;
	private ImageView toright_iv;
	private ImageView sound_iv;
	
	private ImageView head_iv = new ImageView();
	
	private AnchorPane an;
	private Label label;
	private Label label2;
	
	private File file;
	private File file_before;
	
	private ListView<SimpleStringProperty> listview;
	
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		StringBuffer[] stb = init_data();
		
		String filepath = init_mediaplayer(stb[1].toString());
		
		Label artist = new Label();
		Label album = new Label();
		Label title = new Label();
		
		artist.setFont(Font.font(20.0));
		album.setFont(Font.font(20.0));
		title.setFont(Font.font(20.0));
		
		artist.setTextFill(Paint.valueOf("#EEDC82"));
		album.setTextFill(Paint.valueOf("#EEDC82"));
		title.setTextFill(Paint.valueOf("#EEDC82"));
		
		sectrum = new Sectrum(mediaplayer);
		
		Stop[] stops = new Stop[] {new Stop(0, Color.valueOf("#96CDCD")),new Stop(0.5, Color.valueOf("#FA8072"))};
		LinearGradient lg = new LinearGradient(0.5, 1, 0.5, 0, true, CycleMethod.NO_CYCLE, stops);
		HBox hbox = sectrum.createRectangle(400, 115, lg);//Paint.valueOf("#FA8072")
	
		URL url_close = this.getClass().getClassLoader().getResource("img/close.png");
		URL url_zuida = this.getClass().getClassLoader().getResource("img/zuida.png");
		URL url_zuixiao = this.getClass().getClassLoader().getResource("img/zuixiao.png");
		
		URL url_method = this.getClass().getClassLoader().getResource("img/method.png");
		URL url_play = this.getClass().getClassLoader().getResource("img/play.png");
		URL url_playlist = this.getClass().getClassLoader().getResource("img/playlist.png");
		URL url_toleft = this.getClass().getClassLoader().getResource("img/toleft.png");
		URL url_toright = this.getClass().getClassLoader().getResource("img/toright.png");
		URL url_wait = this.getClass().getClassLoader().getResource("img/wait.png");
		URL url_sound = this.getClass().getClassLoader().getResource("img/sound.png");
		
		close_iv = new ImageView(url_close.toExternalForm());
		zuida_iv = new ImageView(url_zuida.toExternalForm());
		zuixiao_iv = new ImageView(url_zuixiao.toExternalForm());
		
		method_iv = new ImageView(url_method.toExternalForm());
		play_iv = new ImageView(url_wait.toExternalForm());
		playlist_iv = new ImageView(url_playlist.toExternalForm());
		toleft_iv = new ImageView(url_toleft.toExternalForm());
		toright_iv = new ImageView(url_toright.toExternalForm());
		sound_iv = new ImageView(url_sound.toExternalForm());
		
		method_iv.setPickOnBounds(true);
		play_iv.setPickOnBounds(true);
		playlist_iv.setPickOnBounds(true);
		toleft_iv.setPickOnBounds(true);
		toright_iv.setPickOnBounds(true);
		sound_iv.setPickOnBounds(true);
		close_iv.setPickOnBounds(true);
		zuida_iv.setPickOnBounds(true);
		zuixiao_iv.setPickOnBounds(true);
		
		method_iv.setSmooth(true);
		play_iv.setSmooth(true);
		playlist_iv.setSmooth(true);
		toleft_iv.setSmooth(true);
		toright_iv.setSmooth(true);
		sound_iv.setSmooth(true);
		head_iv.setSmooth(true);
		close_iv.setSmooth(true);
		zuida_iv.setSmooth(true);
		zuixiao_iv.setSmooth(true);
		
		method_iv.setPreserveRatio(true);
		play_iv.setPreserveRatio(true);
		playlist_iv.setPreserveRatio(true);
		toleft_iv.setPreserveRatio(true);
		toright_iv.setPreserveRatio(true);
		sound_iv.setPreserveRatio(true);
		head_iv.setPreserveRatio(true);
		close_iv.setPreserveRatio(true);
		zuida_iv.setPreserveRatio(true);
		zuixiao_iv.setPreserveRatio(true);
		
		method_iv.setFitHeight(30);
		play_iv.setFitHeight(30);
		playlist_iv.setFitHeight(30);
		toleft_iv.setFitHeight(30);
		toright_iv.setFitHeight(30);
		sound_iv.setFitHeight(30);
		
		close_iv.setFitHeight(20);
		zuida_iv.setFitHeight(20);
		zuixiao_iv.setFitHeight(20);
		
		lyric = new Lyric(media);
		SubScene subscene = lyric.createSubScene();
		
		progress = new Progress(media, mediaplayer, head_iv, 1400, 5, Paint.valueOf("#836FFF"), url_play, play_iv, totaltime, title, artist, album);
		AnchorPane an_progress = progress.createProgress();
		
		Listview view = new Listview(width,stb[0],filepath);
		AnchorPane stackpane = view.createList();
		listview = view.getListView();
		
		label = new Label();
		label2 = new Label("0.00");
		
		totaltime.addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				if(newValue != null)
				{
					label.setText(newValue);
				}
			}
		});
		
		an = new AnchorPane();
		an.setBackground(new Background(new BackgroundFill(Paint.valueOf("#F4F4F4"), new CornerRadii(20), null)));//Paint.valueOf("#F4F4F4")
		an.getChildren().addAll(subscene,hbox,toleft_iv,toright_iv,method_iv,sound_iv,playlist_iv,an_progress,head_iv,zuixiao_iv,zuida_iv,close_iv,play_iv,label,label2,title,artist,album,stackpane);
		
		AnchorPane.setTopAnchor(hbox, 600.0);
		
		AnchorPane.setTopAnchor(subscene, 80.0);
		AnchorPane.setLeftAnchor(subscene, 150.0);
		
		AnchorPane.setTopAnchor(an_progress, 650.0);
		AnchorPane.setLeftAnchor(an_progress, 50.0);
		
		AnchorPane.setTopAnchor(toleft_iv, 700.0);
		AnchorPane.setTopAnchor(play_iv, 700.0);
		AnchorPane.setTopAnchor(toright_iv, 700.0);
		AnchorPane.setTopAnchor(method_iv, 700.0);
		AnchorPane.setTopAnchor(sound_iv, 700.0);
		AnchorPane.setTopAnchor(playlist_iv, 700.0);
		
		AnchorPane.setLeftAnchor(toleft_iv,600.0);
		AnchorPane.setLeftAnchor(play_iv,700.0);
		AnchorPane.setLeftAnchor(toright_iv,800.0);
		AnchorPane.setLeftAnchor(method_iv,900.0);
		AnchorPane.setLeftAnchor(sound_iv,1000.0);
		AnchorPane.setLeftAnchor(playlist_iv,1100.0);
		
		AnchorPane.setTopAnchor(head_iv, 100.0);
		AnchorPane.setLeftAnchor(head_iv, 1000.0);
		
		AnchorPane.setTopAnchor(zuixiao_iv, 25.0);
		AnchorPane.setTopAnchor(zuida_iv, 20.0);
		AnchorPane.setTopAnchor(close_iv, 20.0);
		AnchorPane.setRightAnchor(close_iv, 20.0);
		AnchorPane.setRightAnchor(zuida_iv, 60.0);
		AnchorPane.setRightAnchor(zuixiao_iv, 105.0);
		
		AnchorPane.setTopAnchor(label, 660.0);
		AnchorPane.setTopAnchor(label2, 660.0);
		
		AnchorPane.setLeftAnchor(label, 1420.0);
		AnchorPane.setLeftAnchor(label2, 50.0);
		
		AnchorPane.setTopAnchor(title, 470.0);
		AnchorPane.setTopAnchor(artist, 510.0);
		AnchorPane.setTopAnchor(album, 550.0);
		
		AnchorPane.setLeftAnchor(title, 1100.0);
		AnchorPane.setLeftAnchor(artist, 1100.0);
		AnchorPane.setLeftAnchor(album, 1100.0);
		
		url_close = null;
		url_zuida = null;
		url_zuixiao = null;
		
		url_method = null;
		url_play = null;
		url_playlist = null;
		url_toleft = null;
		url_toright = null;
		url_wait = null;
		url_sound = null;
		
		hbox = null;
		head_iv = null;
		stackpane = null;
		
		stb = null;
		filepath = null;
		
		Scene scene = new Scene(an);
		scene.setFill(Paint.valueOf("#ffffff00"));
		URL css_url = this.getClass().getClassLoader().getResource("css/listview_css");
		scene.getStylesheets().add(css_url.toExternalForm());
		
		css_url = null;
		an_progress = null;
		
		primaryStage.setScene(scene);
		primaryStage.setHeight(height);
		primaryStage.setWidth(width);
		primaryStage.centerOnScreen();
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
		lyric.start();
		
		System.gc();
		
		KeyCombination kccb = new KeyCodeCombination(KeyCode.SPACE, ModifierValue.ANY, ModifierValue.ANY, ModifierValue.ANY, ModifierValue.ANY, ModifierValue.ANY);//快捷键
		scene.getAccelerators().put(kccb, new Runnable()
		{
			@Override
			public void run()
			{  
				if(mediaplayer != null)
				{
					if(sectrum.isPlaying() == false)
					{
						sectrum.play();
						progress.play();
						
						URL url_play = this.getClass().getClassLoader().getResource("img/play.png");
						Image image = new Image(url_play.toExternalForm());
						play_iv.setImage(image);
						
						url_play = null;
						image = null;
					}
					else
					{
						sectrum.pause();
						progress.pause();
						
						URL url_wait = this.getClass().getClassLoader().getResource("img/wait.png");
						Image image = new Image(url_wait.toExternalForm());
						play_iv.setImage(image);
						
						url_wait = null;
						image = null;
					}
				}
			}
		});
		
		scene.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				x = event.getSceneX();
				y = event.getSceneY();
			}
		});
		
		scene.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				primaryStage.setX(event.getScreenX() - x);
				primaryStage.setY(event.getScreenY() - y);
			}
		});
		
		
		play_iv.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				DropShadow ds = new DropShadow();
				play_iv.setEffect(ds);
				ds = null;
			}
		});
		
		play_iv.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(mediaplayer != null)
				{
					if(sectrum.isPlaying() == false)
					{
						sectrum.play();
						progress.play();
						
						URL url_play = this.getClass().getClassLoader().getResource("img/play.png");
						Image image = new Image(url_play.toExternalForm());
						play_iv.setImage(image);
						
						url_play = null;
						image = null;
					}
					else
					{
						sectrum.pause();
						progress.pause();
						
						URL url_wait = this.getClass().getClassLoader().getResource("img/wait.png");
						Image image = new Image(url_wait.toExternalForm());
						play_iv.setImage(image);
						
						url_wait = null;
						image = null;
					}
				}
				play_iv.setEffect(null);
			}
		});
		
		playlist_iv.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				DropShadow ds = new DropShadow();
				playlist_iv.setEffect(ds);
				ds = null;
			}
		});
		
		playlist_iv.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				view.play();
				playlist_iv.setEffect(null);
			}
		});
		
		toleft_iv.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				DropShadow ds = new DropShadow();
				toleft_iv.setEffect(ds);
				ds = null;
			}
		});
		
		toleft_iv.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(listview.getItems().size() != 0)
				{
					URL url_path;
					try
					{
						index--;
						if(index < 0)
						{
							index = listview.getItems().size() - 1;
						}
						
						File file = new File(view.getFilepath() + File.separator + listview.getItems().get(index).get());
						url_path = file.toURI().toURL();
						media = new Media(url_path.toExternalForm());
						if(mediaplayer != null)
						{
							mediaplayer.dispose();
						}
						mediaplayer = new MediaPlayer(media);
						
						sectrum.refresh(mediaplayer);
						progress.refresh(media, mediaplayer);
						lyric.refresh(media);
						MusicPlayer.this.refresh(mediaplayer);
						
						file = null;
						url_path = null;
						media = null;
						
						if(sectrum.isPlaying() == false)
						{
							URL url_play = this.getClass().getClassLoader().getResource("img/play.png");
							Image image = new Image(url_play.toExternalForm());
							play_iv.setImage(image);
							
							url_play = null;
							image = null;
						}
						
						toleft_iv.setEffect(null);
						System.gc();
						
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				else
					toleft_iv.setEffect(null);
			}
		});
		
		toright_iv.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				DropShadow ds = new DropShadow();
				toright_iv.setEffect(ds);
				ds = null;
			}
		});
		
		toright_iv.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(listview.getItems().size() != 0)
				{
					URL url_path;
					try
					{
						index++;
						if(index >= listview.getItems().size())
						{
							index = 0;
						}
						File file = new File(view.getFilepath() + File.separator + listview.getItems().get(index).get());
						url_path = file.toURI().toURL();
						media = new Media(url_path.toExternalForm());
						if(mediaplayer != null)
						{
							mediaplayer.dispose();
						}
						mediaplayer = new MediaPlayer(media);
						
						sectrum.refresh(mediaplayer);
						progress.refresh(media, mediaplayer);
						lyric.refresh(media);
						MusicPlayer.this.refresh(mediaplayer);
						
						file = null;
						url_path = null;
						media = null;
						
						if(sectrum.isPlaying() == false)
						{
							URL url_play = this.getClass().getClassLoader().getResource("img/play.png");
							Image image = new Image(url_play.toExternalForm());
							play_iv.setImage(image);
							
							url_play = null;
							image = null;
						}
						
						toright_iv.setEffect(null);
						System.gc();
						
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				else
					toright_iv.setEffect(null);
			}
		});
		
		sound_iv.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				DropShadow ds = new DropShadow();
				sound_iv.setEffect(ds);
				ds = null;
			}
		});
		
		sound_iv.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				sound_iv.setEffect(null);
			}
		});
		
		close_iv.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				DropShadow ds = new DropShadow();
				ds.setColor(Color.RED);
				close_iv.setEffect(ds);
				ds = null;
			}
		});
		
		close_iv.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				try
				{
					FileOutputStream fip = new FileOutputStream(file_before);
					OutputStreamWriter writer = new OutputStreamWriter(fip, "UTF-8");
					if(mediaplayer != null)
					{
						writer.append((CharSequence) mediaplayer.getMedia().getSource());
						writer.append("\r\n");
						writer.append((CharSequence) view.getFilepath());
						writer.append("\r\n");
						writer.append((CharSequence) String.valueOf(index));
					}
					else
					{
						writer.append("\r\n");
						writer.append((CharSequence) view.getFilepath());
						writer.append("\r\n");
						writer.append("");
					}
					writer.close();
					fip.close();
					
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				
				Platform.exit();
			}
		});
		
		zuixiao_iv.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				DropShadow ds = new DropShadow();
				zuixiao_iv.setEffect(ds);
				ds = null;
			}
		});
		
		zuixiao_iv.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				primaryStage.setIconified(true);
				zuixiao_iv.setEffect(null);
			}
		});
		
		if(mediaplayer != null)
		{
			mediaplayer.setOnEndOfMedia(new Runnable()
			{
				@Override
				public void run()
				{
					lyric.reset();
					mediaplayer.stop();
					mediaplayer.play();
					System.gc();
				}
			});
			
			mediaplayer.currentTimeProperty().addListener(new ChangeListener<Duration>()
			{
				@Override
				public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue)
				{
					old_time = Double.parseDouble(String.format("%.1f", oldValue.toSeconds()));
					new_time = Double.parseDouble(String.format("%.1f", newValue.toSeconds()));
					if(old_time != new_time)
					{
//						DecimalFormat df = new DecimalFormat(".#");//一种格式化 不会四舍五入
						lyric.play_start(old_time);
						label2.setText(String.format("%.2f", oldValue.toMinutes()));
					}
				}
			});
		}
		
		listview.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(event.getClickCount() == 2)
				{
					URL url_path;
					try
					{
						index = listview.getSelectionModel().getSelectedIndex();
						File file = new File(view.getFilepath() + File.separator + listview.getSelectionModel().getSelectedItem().get());
						url_path = file.toURI().toURL();
						media = new Media(url_path.toExternalForm());
						if(mediaplayer != null)
						{
							mediaplayer.dispose();
						}
						mediaplayer = new MediaPlayer(media);
						
						sectrum.refresh(mediaplayer);
						progress.refresh(media, mediaplayer);
						lyric.refresh(media);
						MusicPlayer.this.refresh(mediaplayer);
						
						file = null;
						url_path = null;
						media = null;
						
						if(sectrum.isPlaying() == false)
						{
							URL url_play = this.getClass().getClassLoader().getResource("img/play.png");
							Image image = new Image(url_play.toExternalForm());
							play_iv.setImage(image);
							
							url_play = null;
							image = null;
						}
						
						System.gc();
						
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		});	
	}
	
	public void refresh(MediaPlayer mediaplayer)
	{
		this.mediaplayer = mediaplayer;
		
		mediaplayer.setOnEndOfMedia(new Runnable()
		{
			@Override
			public void run()
			{
				lyric.reset();
				mediaplayer.stop();
				mediaplayer.play();
				System.gc();
			}
		});
		
		mediaplayer.currentTimeProperty().addListener(new ChangeListener<Duration>()
		{
			@Override
			public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue)
			{
				old_time = Double.parseDouble(String.format("%.1f", oldValue.toSeconds()));
				new_time = Double.parseDouble(String.format("%.1f", newValue.toSeconds()));
				if(old_time != new_time)
				{
					lyric.play_start(old_time);
					label2.setText(String.format("%.2f", oldValue.toMinutes()));
				}
			}
		});
	}
	
	public StringBuffer[] init_data() throws IOException
	{
		File folder = new File("./temp");
		if(folder.exists() == false)
		{
			folder.mkdirs();
		}
		
		file = new File("./temp/list.Tai");
		file_before = new File("./temp/before.Tai");
		
		if(file.exists() == false)
		{
			file.createNewFile();
		}
		
		if(file_before.exists() == false)
		{
			file_before.createNewFile();
		}
		
		FileInputStream fip = new FileInputStream(file);
		InputStreamReader reader = new InputStreamReader(fip,"UTF-8");
		StringBuffer sb = new StringBuffer();
		while(reader.ready()) 
		{
			sb.append((char) reader.read());
		}
		
		reader.close();
		fip.close();
		
		FileInputStream fip_before = new FileInputStream(file_before);
		InputStreamReader reader_before = new InputStreamReader(fip_before, "UTF-8");
		StringBuffer sb_before = new StringBuffer();
		while(reader_before.ready()) 
		{
			sb_before.append((char) reader_before.read());
		}
		
		reader_before.close();
		fip_before.close();
		
		folder = null;
		
		StringBuffer[] stb = {sb,sb_before};

		sb = null;
		sb_before = null;
		
		return stb;
	}
	
	public String init_mediaplayer(String value) throws MalformedURLException
	{
		String [] value_list = null;
		
		if(value.equals("") == false)
		{
			value_list = value.split("\r\n");
			if(value_list.length != 0)
			{
				if(value_list[0].equals("") == false)
				{
					media = new Media(value_list[0]);
					mediaplayer = new MediaPlayer(media);
				}
				
				if(value_list[2].equals("") == false)
				{
					index = Integer.parseInt(value_list[2]);
				}
				else
				{
					index = -1;
				}
				return value_list[1];
			}
			else
				return "";
		}
		else
		{
			return "";
		}
	}
}