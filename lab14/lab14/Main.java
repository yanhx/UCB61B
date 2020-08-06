package lab14;

import lab14lib.*;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
//		Generator generator = new SineWaveGenerator(8500);
//		GeneratorPlayer gp = new GeneratorPlayer(generator);
//		gp.play(1000000);
//		Generator generator = new SineWaveGenerator(200);
//		GeneratorDrawer gd = new GeneratorDrawer(generator);
//		gd.draw(4096);
//		Generator generator = new SineWaveGenerator(200);
//		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
//		gav.drawAndPlay(4096, 1000000);

//		Generator g1 = new SineWaveGenerator(200);
//		Generator g2 = new SineWaveGenerator(201);
//		Generator g3 = new SineWaveGenerator(202);
//
//		ArrayList<Generator> generators = new ArrayList<Generator>();
//		generators.add(g1);
//		generators.add(g2);
//		generators.add(g3);
//		MultiGenerator mg = new MultiGenerator(generators);
//
//		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(mg);
//		gav.drawAndPlay(500000, 1000000);

		Generator generator = new StrangeBitwiseGenerator(512);
		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
		gav.drawAndPlay(4096, 1000000);

		//GeneratorDrawer gd = new GeneratorDrawer(generator);
		//gd.draw(4096);
	}
} 