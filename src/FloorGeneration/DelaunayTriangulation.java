package FloorGeneration;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

// we don't need anything outside of this package accessing this stuff

/**
 * A class containing static methods to convert an array of unconnected points into a mesh of triangles.
 * Uses a method known as Delaunay Triangulation, which, in turn, uses an algorithm called the Watson algorithm.
 */
class DelaunayTriangulation {

    // using Wattson's algorithm, do the numbers

    //TODO: i found a paper on this, and a video that seems good
    // redo the entire algorithm
    /**
     * Converts an array of input points into a list of triangles.
     * If (0,0) is in the input, this method will throw an exception.
     *
     * @param inputPoints The input points.
     * @return The list of triangles.
     */
    public static ArrayList<Triangle> triangulate(Point2D.Double... inputPoints) throws OriginInTriangulationInputException {

        // interestingly enough, (0,0) absolutely destroys this.
        // probably something to do with polar coordinate conversion
        //TODO: either way, if (0,0) is in the input, shift everything over by a little
        if (pointArrayContains(inputPoints, new Point2D.Double(0, 0))) {
            throw new OriginInTriangulationInputException("The input for this method cannot contain the point (0,0)");
        }
        //TODO: the numbers do not behave as expected

        // determine the 'bounding box' for the set of points
        double xHi = -999999d;
        double xLo = 999999d;
        double yHi = -999999d;
        double yLo = 999999d;
        for (Point2D point : inputPoints) {
            double x = point.getX();
            double y = point.getY();
            if (x > xHi) {
                xHi = x;
            } else if (x < xLo) {
                xLo = x;
            }
            if (y > yHi) {
                yHi = y;
            } else if (y < yLo) {
                yLo = y;
            }
        }
        // give the bounding box a little wiggle room
        xHi++;
        xLo--;
        yHi++;
        yLo--;

        // sorts the input points in a counterclockwise order about the center of the bounding box
        Point2D.Double boxCenter = new Point2D.Double((xHi+xLo)/2,(yHi+yLo)/2);
        ArrayList<Point2D.Double> inputPointsArray = new ArrayList<>();
        Collections.addAll(inputPointsArray, inputPoints);
        sortPointsCCW(inputPointsArray, boxCenter);
        for (int i = 0; i < inputPoints.length; i++) {
            inputPoints[i] = inputPointsArray.get(i);
        }

        // generate the super triangle around the bounding box
        Triangle superTriangle = new Triangle();
        double rectVerticalSize = yHi - yLo;
        double extraSideLengthHorizontal = rectVerticalSize / Math.sqrt(3d);
        double triangleSideLength = extraSideLengthHorizontal + (xHi - xLo) + extraSideLengthHorizontal;
        double triangleHeight = triangleSideLength * Math.sin(Math.PI / 3);
        superTriangle.addPoint(xLo - extraSideLengthHorizontal, yLo);
        superTriangle.addPoint(xHi + extraSideLengthHorizontal, yLo);
        superTriangle.addPoint((((xHi - xLo) / 2.0) + xLo), triangleHeight + yLo);

        //FIXME: debug
        System.out.println("super triangle");
        System.out.print("\\operatorname{polygon}\\left(");
        System.out.printf("\\left(%f,%f\\right),", superTriangle.xPoints[0], superTriangle.yPoints[0]);
        System.out.printf("\\left(%f,%f\\right),", superTriangle.xPoints[1], superTriangle.yPoints[1]);
        System.out.printf("\\left(%f,%f\\right)", superTriangle.xPoints[2], superTriangle.yPoints[2]);
        System.out.println("\\right)");
        System.out.println();

        // this triangles array will be used as this method's output
        ArrayList<Triangle> triangles = new ArrayList<Triangle>();
        // iteration one is different from the others, do it outside the loop
        Point2D.Double initialPoint = inputPoints[0];
        for (int i = 0; i < 3; i++) {
            Triangle tri = new Triangle();
            tri.addPoint(initialPoint);
            tri.addPoint(superTriangle.getPoint(i));
            tri.addPoint(superTriangle.getPoint(
                    (i + 1 == 3) ? 0 : i + 1
            ));
            triangles.add(tri);
        }

        //FIXME: debug
        for (Triangle tri : triangles) {
            Circle cir = tri.getCircumcircle();
            System.out.printf("\\left(x-%f\\right)^{2}+\\left(y-%f\\right)^{2}=%f^{2}%n", cir.getCenterX(), cir.getCenterY(), cir.getRadius());
            System.out.print("\\operatorname{polygon}\\left(");
            System.out.printf("\\left(%f,%f\\right),", tri.xPoints[0], tri.yPoints[0]);
            System.out.printf("\\left(%f,%f\\right),", tri.xPoints[1], tri.yPoints[1]);
            System.out.printf("\\left(%f,%f\\right)", tri.xPoints[2], tri.yPoints[2]);
            System.out.println("\\right)");
        }
        System.out.println();

        // the rest of the watson algorithm
        /*
        Watson's algorithm to accomplish delauney triangulation.
        Create a super triangle outside of the original set of points
        Then, go point by point.
        On a new point:
            Check if the point is contained within the circumcircle of all existing triangles (stored in a list)
            if a triangle's circumcircle does contain the point, delete the triangle from the list
            then, add the deleted triangle to a new list
            then, create new triangles by connecting the point to all points in the new list
            clear the new list
            end iteration
        continue doing this until all points have been visited
        now, delete every triangle with any of the three points of the super triangle, including the super triangle itself
        return the list of triangles
        */

        // a list of points contained in the super triangle
        ArrayList<Point2D.Double> pointsInSuperTriangle = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            pointsInSuperTriangle.add(superTriangle.getPoint(i));
        }
        sortPointsCCW(pointsInSuperTriangle, boxCenter);

        // most of the Watson algorithm
        for (int i = 1; i < inputPoints.length; i++) {
            // create the queue for connections.
            // this list will hold all the triangles that the current point will connect to
            ArrayList<Triangle> trianglesToConnectTo = new ArrayList<>();
            // get the current point
            Point2D.Double currentPoint = inputPoints[i];
            // check which triangles' circumcircles contain the point, remove those that do from the triangles list
            // also, add these triangles to the trianglesToConnectTo list
            triangles.removeIf(tri -> {
                boolean containsPoint = tri.getCircumcircle().contains(currentPoint);

                if (containsPoint) {
                    trianglesToConnectTo.add(tri);
                }

                return containsPoint;
            });

            // get all points from triangles
            ArrayList<Point2D.Double> allPoints = new ArrayList<>();
            for (Triangle tri : trianglesToConnectTo) {
                for (int j = 0; j < 3; j++) {
                    allPoints.add(tri.getPoint(j));
                }
            }

            // delete duplicate points
            ArrayList<Point2D.Double> validPoints = new ArrayList<>();
            for (Point2D.Double point : allPoints) {
                if (!validPoints.contains(point)) {
                    validPoints.add(point);
                }
            }

            // sort the points in counter-clockwise order centered about the current point
            sortPointsCCW(validPoints, currentPoint);

            // for every point in validPoints, create a triangle consisting of the following points:
            //      the current point
            //      the current point in validPoints (validPoints.get(k))
            //      the next point in validPoints (validPoints.get(k+1))
            //      on the final iteration, the last point will be replaced with the very first point in the list
            for (int k = 0; k < validPoints.size(); k++) {
                Triangle tri = new Triangle();
                tri.addPoint(currentPoint);
                tri.addPoint(validPoints.get(k));
                tri.addPoint(validPoints.get(
                        (k + 1 == validPoints.size()) ? 0 : k + 1
                ));
                // if we make a duplicate triangle, don't add it to the master list
                boolean alreadyExists = false;
                for (Triangle triangle : triangles) {
                    if (tri.equals(triangle)) {
                        alreadyExists = true;
                        break;
                    }
                }
                if (!alreadyExists) {
                    triangles.add(tri);
                }
            }

            //FIXME: debug
            for (Triangle tri : triangles) {
                Circle cir = tri.getCircumcircle();
                System.out.printf("\\left(x-%f\\right)^{2}+\\left(y-%f\\right)^{2}=%f^{2}%n", cir.getCenterX(), cir.getCenterY(), cir.getRadius());
                System.out.print("\\operatorname{polygon}\\left(");
                System.out.printf("\\left(%f,%f\\right),", tri.xPoints[0], tri.yPoints[0]);
                System.out.printf("\\left(%f,%f\\right),", tri.xPoints[1], tri.yPoints[1]);
                System.out.printf("\\left(%f,%f\\right)", tri.xPoints[2], tri.yPoints[2]);
                System.out.println("\\right)");
            }
            System.out.println();


        }

        // remove any triangles that contain the superTriangle's points at all
        for (Point2D.Double point : pointsInSuperTriangle) {
            triangles.removeIf(tri -> {
                for (int j = 0; j < 3; j++) {
                    if (Triangle.floatEquivalence(tri.getPoint(j).x, point.x) &&
                            Triangle.floatEquivalence(tri.getPoint(j).y, point.y)) {
                        return true;
                    }
                }
                return false;
            });
        }


        //TODO: correct offset if (0,0) was in the input
        System.out.println("final triangles array");
        for (Triangle tri : triangles) {
            //tri.translate(-offset,-offset);
            System.out.print("\\operatorname{polygon}\\left(");
            System.out.printf("\\left(%f,%f\\right),", tri.xPoints[0], tri.yPoints[0]);
            System.out.printf("\\left(%f,%f\\right),", tri.xPoints[1], tri.yPoints[1]);
            System.out.printf("\\left(%f,%f\\right)", tri.xPoints[2], tri.yPoints[2]);
            System.out.println("\\right)");
        }
        System.out.printf("%f, %f%n", boxCenter.x,boxCenter.y);
        System.out.print("\\operatorname{polygon}\\left(");
        System.out.printf("\\left(%f,%f\\right),", xHi, yHi);
        System.out.printf("\\left(%f,%f\\right),", xLo, yHi);
        System.out.printf("\\left(%f,%f\\right),", xLo, yLo);
        System.out.printf("\\left(%f,%f\\right)", xHi, yLo);
        System.out.println("\\right)");

        return triangles;

    }

    /**
     * Sorts a list of cartesian points in a counterclockwise order centered on a given reference point.
     *
     * @param input       The list of points to sort.
     * @param originPoint The point to use as reference.
     */
    protected static void sortPointsCCW(ArrayList<Point2D.Double> input, Point2D.Double originPoint) {
        PolarPoint[] polarPoints = new PolarPoint[input.size()];
        for (int i = 0; i < polarPoints.length; i++) {
            polarPoints[i] = new PolarPoint(input.get(i), originPoint);
        }
        Arrays.sort(polarPoints);
        Point2D.Double[] cartesianPoints = new Point2D.Double[polarPoints.length];
        for (int i = 0; i < cartesianPoints.length; i++) {
            cartesianPoints[i] = polarPoints[i].getCartesianPoint();
        }
        input.clear();
        input.addAll(Arrays.asList(cartesianPoints));

    }

    /**
     * Sorts a list of cartesian points in a counterclockwise order centered on the origin point (0,0).
     *
     * @param input The list of points to sort.
     */
    protected static void sortPointsCCW(ArrayList<Point2D.Double> input) {
        PolarPoint[] polarPoints = new PolarPoint[input.size()];
        for (int i = 0; i < polarPoints.length; i++) {
            polarPoints[i] = new PolarPoint(input.get(i));
        }
        Arrays.sort(polarPoints);
        Point2D.Double[] cartesianPoints = new Point2D.Double[polarPoints.length];
        for (int i = 0; i < cartesianPoints.length; i++) {
            cartesianPoints[i] = polarPoints[i].getCartesianPoint();
        }
        input.clear();
        input.addAll(Arrays.asList(cartesianPoints));
    }

    /**
     * Checks if an array of 2D points contains a given point.
     *
     * @param array The array to check.
     * @param point The point to look for.
     * @return True or False.
     */
    private static boolean pointArrayContains(Point2D.Double[] array, Point2D.Double point) {
        for (Point2D.Double aDouble : array) {
            if (Triangle.floatEquivalence(aDouble.x, point.x) && Triangle.floatEquivalence(aDouble.y, point.y)) {
                return true;
            }
        }
        return false;
    }

}

