public class NBody {
    public static double readRadius(String file) {
        In in  = new In(file);
        in.readLine();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String file) {
        In in  = new In(file);
        int N = in.readInt();
        Planet[] ret = new Planet[N];
        in.readDouble();
        for (int i = 0; i < N; i++) {
            double xP = in.readDouble(), yP = in.readDouble(), xV = in.readDouble(), yV = in.readDouble(), m = in.readDouble();
            String img = in.readString();
            ret[i] = new Planet(xP, yP, xV, yV, m, img);
        }
        return  ret;
    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]), dt = Double.parseDouble(args[1]);
        String filename = args[2];

        double R = readRadius(filename);
        Planet[] allP = readPlanets(filename);

        StdDraw.setScale(-R, R);
        StdDraw.clear();
        StdDraw.picture(0, 0, "images\\starfield.jpg", 2*R, 2*R);
        for (Planet p : allP)
            p.draw();

        StdDraw.enableDoubleBuffering();
        for (double time = 0; time < T; time += dt) {
            double[] xForces = new double[allP.length], yForces = new double[allP.length];

            for (int i = 0; i < allP.length; i++) {
                xForces[i] = allP[i].calcNetForceExertedByX(allP);
                yForces[i] = allP[i].calcNetForceExertedByY(allP);
            }
            for (int i = 0; i < allP.length; i++)
                allP[i].update(dt, xForces[i], yForces[i]);
            //StdDraw.clear();
            StdDraw.picture(0, 0, "images\\starfield.jpg", 2*R, 2*R);
            for (Planet p : allP)
                p.draw();
            StdDraw.show();
            StdDraw.pause(10);
        }

        StdOut.printf("%d\n", allP.length);
        StdOut.printf("%.2e\n", R);
        for (int i = 0; i < allP.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    allP[i].xxPos, allP[i].yyPos, allP[i].xxVel,
                    allP[i].yyVel, allP[i].mass, allP[i].imgFileName);
        }
    }
}
