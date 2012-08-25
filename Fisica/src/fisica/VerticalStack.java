package fisica;
import javax.swing.JFrame;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
//import org.jbox2d.testbed.framework.TestList;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.TestbedSetting;
import org.jbox2d.testbed.framework.TestbedSetting.SettingType;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

public class VerticalStack extends TestbedTest {

	public static void main(String [ ] args)
	{
		TestbedModel model = new TestbedModel();         // create our model

		// add tests
		//TestList.populateModel(model); // populate the provided testbed tests
		model.addCategory("Custom Tests"); // add a category
		model.addTest(new VerticalStack()); // add our test

		// add our custom setting "My Range Setting", with a default value of 10, between 0 and 20
		model.getSettings().addSetting(
				new TestbedSetting("My Range Setting", SettingType.ENGINE, 10, 0, 20)
				);

		TestbedPanel panel = new TestPanelJ2D(model);    // create our testbed panel

		JFrame testbed = new TestbedFrame(model, panel); // put both into our testbed frame
		// etc
		testbed.setVisible(true);
		testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	private static final long BULLET_TAG = 1;

	public static final int e_columnCount = 5;
	public static final int e_rowCount = 16;

	Body m_bullet;

	@Override
	public Long getTag(Body argBody) {
		if(argBody == m_bullet){
			return BULLET_TAG;
		}
		return super.getTag(argBody);
	}

	@Override
	public void processBody(Body argBody, Long argTag) {
		if(argTag == BULLET_TAG){
			m_bullet = argBody;
			return;
		}
		super.processBody(argBody, argTag);
	}

	@Override
	public boolean isSaveLoadEnabled() {
		return true;
	}

	@Override
	public void initTest(boolean deserialized) {
		if(deserialized){
			return;
		}

		{
			BodyDef bd = new BodyDef();
			Body ground = getWorld().createBody(bd);

			PolygonShape shape = new PolygonShape();
			shape.setAsEdge(new Vec2(-15.0f, 0.0f), new Vec2(15.0f, 0.0f));
			ground.createFixture(shape, 0.0f);

			shape.setAsEdge(new Vec2(20.0f, 0.0f), new Vec2(20.0f, 20.0f));
			ground.createFixture(shape, 0.0f);
		}

		float xs[] = new float[] { 0.0f, -10.0f, -5.0f, 5.0f, 10.0f };

		for (int j = 0; j < e_columnCount; ++j) {
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(0.5f, 0.5f);

			FixtureDef fd = new FixtureDef();
			fd.shape = shape;
			fd.density = 1.0f;
			fd.friction = 0.3f;

			for (int i = 0; i < e_rowCount; ++i) {
				BodyDef bd = new BodyDef();
				bd.type = BodyType.DYNAMIC;
				float x = 0.0f;
				bd.position.set(xs[j] + x, 0.752f + 1.54f * i);
				Body body = getWorld().createBody(bd);

				body.createFixture(fd);
			}
		}

		m_bullet = null;
	}

	@Override
	public void keyPressed(char argKeyChar, int argKeyCode) {
		switch (argKeyChar) {
		case ',':
			/*if (m_bullet != null) {
				getWorld().destroyBody(m_bullet);
				m_bullet = null;
			}*/

		{
			CircleShape shape = new CircleShape();
			shape.m_radius = 0.9f;

			FixtureDef fd = new FixtureDef();
			fd.shape = shape;
			fd.density = 20.0f;
			fd.restitution = 0.05f;

			BodyDef bd = new BodyDef();
			bd.type = BodyType.DYNAMIC;
			bd.bullet = true;
			bd.position.set(-20.0f, 5.0f);

			m_bullet = getWorld().createBody(bd);
			m_bullet.createFixture(fd);

			m_bullet.setLinearVelocity(new Vec2(30.0f, 10.0f));
		}
		break;
		}
	}

	@Override
	public void step(TestbedSettings settings) {
		super.step(settings);
		addTextLine("Press ',' to launch bullet.");
	}

	@Override
	public String getTestName() {
		return "Vertical Stack";
	}
}