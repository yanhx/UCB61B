public class Planet {
    private static final double G = 6.67e-11;
    public  double xxPos, yyPos, xxVel, yyVel, mass;
    public  String imgFileName;

    public Planet(double xP, double yP, double xV,
                  double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        return Math.sqrt((this.xxPos - p.xxPos)*(this.xxPos - p.xxPos) + (this.yyPos - p.yyPos)*(this.yyPos - p.yyPos));
    }

    public double calcForceExertedBy(Planet p) {
        if (this.equals(p)) return 0;
        double r = calcDistance(p);
        return G * this.mass * p.mass / (r*r);
    }

    public double calcForceExertedByX(Planet p) {
        if (this.equals(p)) return 0;
        double r = calcDistance(p);
        return calcForceExertedBy(p) * (p.xxPos - this.xxPos) / r;
    }

    public double calcForceExertedByY(Planet p) {
        if (this.equals(p)) return 0;
        double r = calcDistance(p);
        return calcForceExertedBy(p) * (p.yyPos - this.yyPos) / r;
    }

    public double calcNetForceExertedByX(Planet[] allP) {
        double f = 0;
        for (Planet p : allP)
            f += calcForceExertedByX(p);
        return f;
    }

    public double calcNetForceExertedByY(Planet[] allP) {
        double f = 0;
        for (Planet p : allP)
            f += calcForceExertedByY(p);
        return f;
    }

    public void update(double dt, double fx, double fy) {
        double ax = fx / mass, ay = fy / mass;
        xxVel += dt * ax;
        yyVel += dt * ay;
        xxPos += dt * xxVel;
        yyPos += dt * yyVel;
    }

    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images\\" + imgFileName);
    }

}
