package com.mygdx.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class GUI {
	public Stage stage;
	Skin skin;
	Table table;
	final String[] size = new String[1];


	public GUI(){
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage(new ScreenViewport());
		table =  new Table();
	}


	public void textBox(){
		Gdx.input.setInputProcessor(stage);
		stage.addActor(table);
		final TextField textSize = new TextField("", skin);
		textSize.setMessageText("size");
		textSize.setTextFieldListener(new TextFieldListener() {
			public void keyTyped (TextField textField, char key) {
				if (key == '\n')
					size[0] = textSize.getText();
			}
		});
		Window window = new Window("SizeWindow", skin);
		window.getTitleTable().add(new TextButton("X", skin)).height(window.getPadTop());
		window.setPosition(Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight() / 2);
		window.defaults().spaceBottom(10);
		window.row().fill().expandX();
		window.add(textSize).minWidth(100).expandX().fillX().colspan(2);
		window.pack();
		stage.addActor(window);
	}

	public int[] textBoxDetector(){
		if (size[0] != null) {
			String[] numbers = size[0].split("x");
			if (numbers.length != 2)
				System.err.println("ERROR, you put more than or less than an 'x'.");
			else {
				try {
					int[] size = new int[2];
					size[0] = Integer.parseInt(numbers[0]);
					size[1] = Integer.parseInt(numbers[1]);
					return size;
				} catch (NumberFormatException e){
					System.err.println("Not numeric data introduced withing the textbox");
				}
			}
		}
		return new int[2];
	}








	// tests from libgdx's github -----

	public void lastTetCode(){stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		// Gdx.graphics.setVSync(false);

		Table container = new Table();
		stage.addActor(container);
		container.setFillParent(true);

		Table table = new Table();
		// table.debug();

		final ScrollPane scroll = new ScrollPane(table, skin);

		InputListener stopTouchDown = new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return false;
			}
		};

		table.pad(10).defaults().expandX().space(4);
		for (int i = 0; i < 100; i++) {
			table.row();
			table.add(new Label(i + "uno", skin)).expandX().fillX();

			TextButton button = new TextButton(i + "dos", skin);
			table.add(button);
			button.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					System.out.println("flooredClick " + x + ", " + y);
				}
			});

			Slider slider = new Slider(0, 100, 1, false, skin);
			slider.addListener(stopTouchDown); // Stops touchDown events from propagating to the FlickScrollPane.
			table.add(slider);

			table.add(new Label(i + "tres long0 long1 long2 long3 long4 long5 long6 long7 long8 long9 long10 long11 long12", skin));
		}

		final TextButton flickButton = new TextButton("Flick Scroll", skin.get("toggle", TextButtonStyle.class));
		flickButton.setChecked(true);
		flickButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				scroll.setFlickScroll(flickButton.isChecked());
			}
		});

		final TextButton fadeButton = new TextButton("Fade Scrollbars", skin.get("toggle", TextButtonStyle.class));
		fadeButton.setChecked(true);
		fadeButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				scroll.setFadeScrollBars(fadeButton.isChecked());
			}
		});

		final TextButton smoothButton = new TextButton("Smooth Scrolling", skin.get("toggle", TextButtonStyle.class));
		smoothButton.setChecked(true);
		smoothButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				scroll.setSmoothScrolling(smoothButton.isChecked());
			}
		});

		final TextButton onTopButton = new TextButton("Scrollbars On Top", skin.get("toggle", TextButtonStyle.class));
		onTopButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				scroll.setScrollbarsOnTop(onTopButton.isChecked());
			}
		});

		container.add(scroll).expand().fill().colspan(4);
		container.row().space(10).padBottom(10);
		container.add(flickButton).right().expandX();
		container.add(onTopButton);
		container.add(smoothButton);
		container.add(fadeButton).left().expandX();}


	public void text(){
		ShapeRenderer renderer = new ShapeRenderer();
		skin.getAtlas().getTextures().iterator().next().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		skin.getFont("default-font").getData().markupEnabled = true;
		skin.getFont("default-font").getData().setScale(1);
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		// skin.getFont("default-font").getData().getGlyph('T').xoffset = -20;
		skin.getFont("default-font").getData().getGlyph('B').setKerning('B', -5);

		Label label;

		Table table = new Table().debug();

		table.add(new Label("This is regular text.", skin)).row();
		table.add(new Label("This is regular text\nwith a newline.", skin)).row();

		label = new Label("This is [RED]regular text\n\nwith newlines,\naligned bottom, right.", skin);
		label.setColor(Color.GREEN);
		label.setAlignment(Align.bottom | Align.right);
		table.add(label).minWidth(200).minHeight(110).fill().row();

		label = new Label("This is regular text with NO newlines, wrap enabled and aligned bottom, right.", skin);
		label.setWrap(true);
		label.setAlignment(Align.bottom | Align.right);
		table.add(label).minWidth(200 ).minHeight(110 ).fill().row();

		label = new Label("This is regular text with\n\nnewlines, wrap\nenabled and aligned bottom, right.", skin);
		label.setWrap(true);
		label.setAlignment(Align.bottom | Align.right);
		table.add(label).minWidth(200 ).minHeight(110 ).fill().row();

		table.setPosition(50, 40 + 25 );
		table.pack();
		stage.addActor(table);

		//

		table = new Table().debug();
		stage.addActor(table);

		table.add(new Label("This is regular text.", skin)).minWidth(200 ).row();

		// The color markup text should match the uncolored text exactly.
		label = new Label("AAA BBB CCC DDD EEE", skin);
		table.add(label).align(Align.left).row();

		label = new Label("AAA B[RED]B[]B CCC DDD EEE", skin);
		table.add(label).align(Align.left).row();

		label = new Label("[RED]AAA [BLUE]BBB [RED]CCC [BLUE]DDD [RED]EEE", skin);
		table.add(label).align(Align.left).row();

		label = new Label("AAA BBB CCC DDD EEE", skin);
		label.setWrap(true);
		table.add(label).align(Align.left).width(150 ).row();

		label = new Label("[RED]AAA [BLUE]BBB [RED]CCC [BLUE]DDD [RED]EEE", skin);
		label.setWrap(true);
		table.add(label).align(Align.left).width(150 ).row();

		table.setPosition(50 + 250 , 40 + 25 );
		table.pack();
		stage.addActor(table);
	}


	public void testImageIGDunno(){
		TextureRegion image2 = new TextureRegion(new Texture(Gdx.files.internal("Sprites/char.jpg")));

		Gdx.input.setInputProcessor(stage);

		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.addActor(table);
		table.debug();

		Image image = new Image(image2);
		image.setScaling(Scaling.fill);
		table.add(image).width(image2.getRegionWidth()).height(image2.getRegionHeight());
	}

	public void anotherTestUI(){
		Object[] listEntries = {"This is a list entry1", "And another one1", "The meaning of life1", "Is hard to come by1",
				"This is a list entry2", "And another one2", "The meaning of life2", "Is hard to come by2", "This is a list entry3",
				"And another one3", "The meaning of life3", "Is hard to come by3", "This is a list entry4", "And another one4",
				"The meaning of life4", "Is hard to come by4", "This is a list entry5", "And another one5", "The meaning of life5",
				"Is hard to come by5"};
		Texture texture1 = new Texture(Gdx.files.internal("Sprites/char.jpg"));
		Texture texture2 = new Texture(Gdx.files.internal("Sprites/Grass.png"));
		TextureRegion image = new TextureRegion(texture1);
		TextureRegion imageFlipped = new TextureRegion(image);
		imageFlipped.flip(true, true);
		TextureRegion image2 = new TextureRegion(texture2);
		Gdx.input.setInputProcessor(stage);

		stage.setDebugAll(true);

		ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(skin.get(Button.ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(image);
		style.imageDown = new TextureRegionDrawable(imageFlipped);
		ImageButton iconButton = new ImageButton(style);

		Button buttonMulti = new TextButton("Multi\nLine\nToggle", skin, "toggle");
		Button imgButton = new Button(new Image(image), skin);
		Button imgToggleButton = new Button(new Image(image), skin, "toggle");

		Label myLabel = new Label("this is some text.", skin);
		myLabel.setWrap(true);

		Table t = new Table();
		t.row();
		t.add(myLabel);

		t.layout();

		final CheckBox checkBox = new CheckBox(" Continuous rendering", skin);
		checkBox.setChecked(true);
		final Slider slider = new Slider(0, 10, 1, false, skin);
		slider.setAnimateDuration(0.3f);
		TextField textfield = new TextField("", skin);
		textfield.setMessageText("Click here!");
		textfield.setAlignment(Align.center);
		final SelectBox selectBox = new SelectBox(skin);
		selectBox.setAlignment(Align.right);
		selectBox.getList().setAlignment(Align.right);
		selectBox.getStyle().listStyle.selection.setRightWidth(10);
		selectBox.getStyle().listStyle.selection.setLeftWidth(20);
		selectBox.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println(selectBox.getSelected());
			}
		});
		selectBox.setItems("Android1", "Windows1 long text in item", "Linux1", "OSX1", "Android2", "Windows2", "Linux2", "OSX2",
				"Android3", "Windows3", "Linux3", "OSX3", "Android4", "Windows4", "Linux4", "OSX4", "Android5", "Windows5", "Linux5",
				"OSX5", "Android6", "Windows6", "Linux6", "OSX6", "Android7", "Windows7", "Linux7", "OSX7", "Windows11");
		selectBox.setSelected("Windows11");
		Image imageActor = new Image(image2);
		ScrollPane scrollPane = new ScrollPane(imageActor);
		List list = new List(skin);
		list.setItems(listEntries);
		list.getSelection().setMultiple(true);
		list.getSelection().setRequired(false);
		// list.getSelection().setToggle(true);
		ScrollPane scrollPane2 = new ScrollPane(list, skin);
		scrollPane2.setFlickScroll(false);
		Label minSizeLabel = new Label("minWidth cell", skin); // demos SplitPane respecting widget's minWidth
		Table rightSideTable = new Table(skin);
		rightSideTable.add(minSizeLabel).growX().row();
		rightSideTable.add(scrollPane2).grow();
		SplitPane splitPane = new SplitPane(scrollPane, rightSideTable, false, skin, "default-horizontal");
		Label fpsLabel = new Label("fps:", skin);

		// configures an example of a TextField in password mode.
		final Label passwordLabel = new Label("Textfield in password mode: ", skin);
		final TextField passwordTextField = new TextField("", skin);
		passwordTextField.setMessageText("password");
		passwordTextField.setPasswordCharacter('L');
		passwordTextField.setPasswordMode(true);

		buttonMulti.addListener(new TextTooltip(
				"This is a tooltip! This is a tooltip! This is a tooltip! This is a tooltip! This is a tooltip! This is a tooltip!",
				skin));
		Table tooltipTable = new Table(skin);
		tooltipTable.pad(10).background("default-round");
		tooltipTable.add(new TextButton("Fancy tooltip!", skin));
		imgButton.addListener(new Tooltip(tooltipTable));

		// window.debug();
		Window window = new Window("Dialog", skin);
		window.getTitleTable().add(new TextButton("X", skin)).height(window.getPadTop());
		window.setPosition(0, 0);
		window.defaults().spaceBottom(10);
		window.row().fill().expandX();
		window.add(iconButton);
		window.add(buttonMulti);
		window.add(imgButton);
		window.add(imgToggleButton);
		window.row();
		window.add(checkBox);
		window.add(slider).minWidth(100).fillX().colspan(3);
		window.row();
		window.add(selectBox).maxWidth(100);
		window.add(textfield).minWidth(100).expandX().fillX().colspan(3);
		window.row();
		window.add(splitPane).fill().expand().colspan(4).maxHeight(200);
		window.row();
		window.add(passwordLabel).colspan(2);
		window.add(passwordTextField).minWidth(100).expandX().fillX().colspan(2);
		window.row();
		window.add(fpsLabel).colspan(4);
		window.pack();
		stage.addActor(window);

		textfield.setTextFieldListener(new TextFieldListener() {
			public void keyTyped (TextField textField, char key) {
				if (key == '\n') textField.getOnscreenKeyboard().show(false);
			}
		});

		slider.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("UITest", "slider: " + slider.getValue());
			}
		});

		iconButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				new Dialog("Some Dialog", skin, "dialog") {
					protected void result (Object object) {
						System.out.println("Chosen: " + object);
					}
				}.text("Are you enjoying this demo?").button("Yes", true).button("No", false).key(Keys.ENTER, true)
						.key(Keys.ESCAPE, false).show(stage);
			}
		});

		checkBox.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.graphics.setContinuousRendering(checkBox.isChecked());
			}
		});
		System.out.println(textfield.getText());
	}




	public void testUI() {
		Gdx.input.setInputProcessor(stage);

		// A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
		// recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.

		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		// Store the default libGDX font under the name "default".
		skin.add("default", new BitmapFont());

		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

		// Create a table that fills the screen. Everything else will go inside this table.
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		// Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
		final TextButton button = new TextButton("Click me!", skin);
		table.add(button);

		// Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
		// Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
		// ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
		// revert the checked state.
		button.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Clicked! Is checked: " + button.isChecked());
				button.setText("Good job!");
			}
		});

		// Add an image actor. Have to set the size, else it would be the size of the drawable (which is the 1x1 texture).
		table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);
	}



	public void tesTable(){
		Gdx.input.setInputProcessor(stage);
		Label nameLabel = new Label("Name:", skin);
		TextField nameText = new TextField("", skin);
		Label addressLabel = new Label("Address:", skin);
		TextField addressText = new TextField("", skin);
		Table table = new Table();
		stage.addActor(table);
		table.setSize(260, 195);
		table.setPosition(190, 142);
		table.debug();

		TextureRegion upRegion = skin.getRegion("default-slider-knob");
		TextureRegion downRegion = skin.getRegion("default-slider-knob");
		BitmapFont buttonFont = skin.getFont("default-font");

		TextButton button = new TextButton("Button 1", skin);
		button.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("touchDown 1");
				return false;
			}
		});
		table.add(button);

		Table table2 = new Table();
		stage.addActor(table2);
		table2.setFillParent(true);
		table2.bottom();

		TextButton button2 = new TextButton("Button 2", skin);
		button2.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("2!");
			}
		});
		button2.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("touchDown 2");
				return false;
			}
		});
		table2.add(button2);
	}


	public void testButton(){
		TextButton button = new TextButton("Test omg", skin, "default");
		button.setWidth((float) Gdx.graphics.getWidth() /10);
		button.setHeight((float) Gdx.graphics.getHeight() /20);
		button.align(Align.center | Align.top);
		Dialog dialog = new Dialog("Test message",skin);

		button.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				dialog.show(stage);
				System.out.println(1);
			}
		});

		button.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				dialog.show(stage);
				System.out.println(2);
			}
		});

		stage.addActor(button);
		Gdx.input.setInputProcessor(stage);
	}

	// end test ---

	public void renderGUI(){
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		textBoxDetector();
	}



}
