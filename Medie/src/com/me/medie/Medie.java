package com.me.medie;

import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import jxl.*;
import jxl.read.biff.BiffException;

public class Medie implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	public Stage stage;
	Skin skin;
	Label l1;
	
	int SW, SH;
	
	TextButton button;
	TextField tb_a, tb_b, tb_medie;
	
	@Override
	public void create() {		
		SW = Gdx.graphics.getWidth();
		SH = Gdx.graphics.getHeight();
		
		stage = new Stage(SW, SH, true);
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		camera = new OrthographicCamera(SW, SH);
		camera.translate(SW/2, SH/2);
		camera.update();
		batch = new SpriteBatch();
		
		Texture.setEnforcePotImages(false);
		
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		sprite = new Sprite(texture);
		sprite.setBounds(0, 0, 44, 44);
		
		skin = new Skin();
		LabelStyle labelstyle = new LabelStyle();

		BitmapFont fontarial15 = new BitmapFont();
		BitmapFont fontarial24 = new BitmapFont(Gdx.files.internal("data/arial24.fnt"));
		BitmapFont fontarial32 = new BitmapFont(Gdx.files.internal("data/arial32.fnt"));
		
		skin.add("default", fontarial15);
		labelstyle.font = skin.getFont("default");

		skin.add("Red", Color.RED);
		
		labelstyle.fontColor = skin.getColor("Red");
		
		skin.add("default", labelstyle);

		l1 = new Label("Hello world!", skin);
		l1.setPosition(0, SH - l1.getHeight());
		l1.setRotation(25);
		l1.setStyle(labelstyle);
		
		TextButtonStyle btnstyle = new TextButtonStyle();
		btnstyle.font = skin.getFont("default");
		btnstyle.fontColor = Color.BLACK;

		TextureAtlas btn_atlas = new TextureAtlas("data/button.pak");
		skin.addRegions(btn_atlas);
		
		btnstyle.up = skin.getDrawable("btn_up");
		btnstyle.down = skin.getDrawable("btn_down");
		
		skin.add("default", btnstyle);
		
		button = new TextButton("Calculeaza", btnstyle);
		button.setPosition(SW / 2 - button.getWidth(), 0);
		button.setSize(SW / 4, SH / 8);
		
		stage.addActor(button);
		stage.addActor(l1);
		
		TextFieldStyle tfstyle = new TextFieldStyle();
		tfstyle.font = fontarial15;
		tfstyle.fontColor = skin.getColor("Red");
		skin.add("default", tfstyle);
		skin.add("textfieldbackground", new Texture(Gdx.files.internal("data/textfield.png")));
		
		tfstyle.background = skin.getDrawable("textfieldbackground");
		tfstyle.background.setLeftWidth(10);
		
		tb_a = new TextField("", skin);
		tb_b = new TextField("", skin);
		tb_medie = new TextField("", skin);

		tb_a.setTextFieldListener(new TextFieldListener() {

            @Override
            public void keyTyped(TextField textField, char key) {
                    l1.setText(tb_a.getText());
            }
        });
		
		button.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button){
				try
				{
					tb_medie.setText(getavg(
							tb_a.getText(),
							tb_b.getText()));
				}
				catch(NumberFormatException e1){
					tb_medie.setText("Introdu valori!");
				}
				
				return true;
			}
		});

		tb_a.setSize(50, 30);
		tb_b.setSize(50, 30);
		tb_medie.setSize(120, 30);
		tb_a.setPosition(80, 210);
		tb_b.setPosition(80, 160);
		tb_medie.setPosition(80, 100);
		
		stage.addActor(tb_a);
		stage.addActor(tb_b);
		stage.addActor(tb_medie);
		
		String workpath = "C:/Users/Attila/workspace/Medie-android/assets/data/Excel1.xls";
		System.out.println(workpath);
		File inworkbook = new File(workpath);
		
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(inworkbook);
		} catch (BiffException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Sheet sheet1 = wb.getSheet(0);
		Cell cell1 = sheet1.getCell(1, 1);
		System.out.println(cell1.getContents());
	}
	
	public String getavg(String a, String b){
		float aa = Float.parseFloat(a);
		float bb = Float.parseFloat(b);
		float avg = (aa + bb) / 2;
		return Float.toString(avg);
	}
	
	boolean istouched;

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
		skin.dispose();
	}
	
	void increment(TextField a){
		int aa = Integer.parseInt(tb_a.getText());
		aa++;
		tb_a.setText(Integer.toString(aa));
	}
	
	public void update(){
		int x, y;
		x = Gdx.input.getX();
		y = Gdx.input.getY();
		y = SH - y;
			
		String text = Integer.toString(x) + " " + Integer.toString(y);
		l1.setText(text);

		stage.act(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void render() {		
		update();
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		SW = width;
		SH = height;
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
