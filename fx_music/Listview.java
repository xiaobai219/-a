package fx_music;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 * 用来装歌曲列表的类
 * @author 空太
 *
 */
public class Listview
{
	private double width;
	private String filepath;
	private StringBuffer sb;
	
	private ObservableList<SimpleStringProperty> obslist;
	private ListView<SimpleStringProperty> list;
	private AnchorPane an;
	
	private boolean outside;
	private boolean outside2;
	
	private TextField tf;
	private Button bu;
	private Button bu2;
	
	public Listview(double width,StringBuffer sb,String filepath)
	{
		this.width = width;
		this.sb = sb;
		this.filepath = filepath;
	}
	
	/**
	 * 创建一个面板
	 * 里面是列表和两个按钮
	 * 运行看效果
	 * @return
	 */
	public AnchorPane createList()
	{	
		outside = true;
		outside2 = true;
		
		an = new AnchorPane();
		an.setTranslateX(width);
		an.setTranslateY(100);
		
		tf = new TextField();
		tf.setFocusTraversable(false);
		tf.setStyle("-fx-background-color:#9ACD32");
		tf.setPrefWidth(300);
		tf.setPrefHeight(30);
		tf.setTranslateX(300);
		tf.setTranslateY(30);
		
		if(filepath != null)
		{
			tf.setText(filepath);
		}
		
		obslist = FXCollections.observableArrayList();
		list = new ListView<SimpleStringProperty>(obslist);
		
		list.setFocusTraversable(false);
		list.setStyle("-fx-background-color:#B4EEB4");
		list.setPrefHeight(510);
		list.setPrefWidth(300);
		
		list.setTranslateY(30);
		
		list.setCellFactory(TextFieldListCell.forListView(new StringConverter<SimpleStringProperty>()
		{
			@Override
			public String toString(SimpleStringProperty object)
			{
				return object.get().substring(0, object.get().length() - 4);
			}

			@Override
			public SimpleStringProperty fromString(String string)
			{
				return null;
			}
		}));
		
		
		list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SimpleStringProperty>()
		{
			@Override
			public void changed(ObservableValue<? extends SimpleStringProperty> observable,
					SimpleStringProperty oldValue, SimpleStringProperty newValue)
			{
				if(newValue != null)
				{
					Tooltip too = new Tooltip(newValue.get());
					Tooltip.install(list, too);
					
					too = null;
				}
			}
		});
		
		bu = new Button("扫描本地音乐");
		bu.setFocusTraversable(false);
		bu.setPrefWidth(150);
		bu.setStyle("-fx-background-color:#FFFFFF");
		
		bu.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				bu.setStyle("-fx-background-color:#E3E3E3");
			}
		});
		
		bu.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(tf.getText().equals("") != true)
				{
					obslist.clear();
					filepath = tf.getText();
					String pathname = "./temp/";
					
					File file = new File(filepath);
					
					File data_temp = new File(pathname);
					data_temp.mkdirs();
					
					File data_file = new File(pathname + "list.Tai");
					FileOutputStream fop = null;
					OutputStreamWriter writer = null;
					try
					{
						fop = new FileOutputStream(data_file);
						writer = new OutputStreamWriter(fop, "UTF-8");
					} catch (Exception e)
					{
						e.printStackTrace();
					}
			
					
					String[] files = file.list();
					
					for(String t : files)
					{
						if(t.endsWith(".mp3") || t.endsWith(".m4a") || t.endsWith(".wav"))
						{
							SimpleStringProperty name = new SimpleStringProperty(t);
							obslist.add(name);
							try
							{
								writer.append((CharSequence) name.get());
								writer.append("\r\n");
							} catch (IOException e)
							{
								e.printStackTrace();
							}
							
							name = null;
						}
						t = null;
					}
					
					bu.setStyle("-fx-background-color:#FFFFFF");
					
					try
					{
						writer.close();
						fop.close();
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					
					pathname = null;
					file = null;
					files = null;
					data_temp = null;
					data_file = null;
				}
			}
		});
		
		bu2 = new Button("选择文件夹");
		bu2.setFocusTraversable(false);
		bu2.setPrefWidth(150);
		bu2.setTranslateX(150);
		bu2.setStyle("-fx-background-color:#FFFFFF");
		
		an.getChildren().addAll(bu,bu2,list);
		
		bu2.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				bu2.setStyle("-fx-background-color:#E3E3E3");
			}
		});
		
		bu2.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(outside2 == true)
				{	
					an.getChildren().add(tf);
					
					TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5));
					tt.setNode(tf);
					tt.setFromX(300);
					tt.setToX(0);
					tt.play();
					
					tf.requestFocus();
			
					outside2 = false;
					tt = null;
				}
				else
				{		
					an.getChildren().remove(tf);
					outside2 = true;
				}
				
				bu2.setStyle("-fx-background-color:#FFFFFF");
			}
		});
		
		init_data();
		
		return an;
	}

	/**
	 * 歌曲列表动画出现和消失效果
	 * 内部调用
	 */
	public void play()
	{
		if(outside == true)
		{
			TranslateTransition translate = new TranslateTransition();
			translate.setNode(an);
			translate.setDuration(Duration.seconds(0.5));
			translate.setFromX(width);
			translate.setToX(width - list.getPrefWidth()); 
			translate.play();

			outside = false;
			
			translate = null;
		}
		else
		{
			TranslateTransition translate = new TranslateTransition();
			translate.setNode(an);
			translate.setDuration(Duration.seconds(0.5));
			translate.setFromX(width - list.getPrefWidth());
			translate.setToX(width); 
			translate.play();
			
			outside = true;
			
			translate = null;
			
			System.gc();
		}
	}
	/**
	 * 返回歌曲所在的文件夹
	 * @return
	 */
	public String getFilepath()
	{
		return filepath;
	}
	
	/**
	 * 返回歌曲的列表
	 * @return
	 */
	public ListView<SimpleStringProperty> getListView()
	{
		return list;
	}
	
	/**
	 * 内部调用
	 * 初始化列表数据
	 */
	protected void init_data()
	{
		String data = sb.toString();
		String[] list_data = data.split("\r\n");
		for(int i=0;i<list_data.length;i++)
		{
			if(list_data[i].equals("") == false)
			{
				SimpleStringProperty one_data = new SimpleStringProperty(list_data[i]);
				obslist.add(one_data);
				
				one_data = null;
			}
		}
	}
}