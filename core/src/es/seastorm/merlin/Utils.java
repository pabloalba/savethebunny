package es.seastorm.merlin;

import com.badlogic.gdx.math.Vector2;

import es.seastorm.merlin.gameobjects.Animal;


public class Utils {

    private final static float INIT_X = (Constants.WIDTH - (Constants.FLOOR_SIZE * Constants.LABYRINT_WIDTH)) / 2;
    private final static float INIT_Y = Constants.HEIGHT - Constants.FLOOR_SIZE;

    public static Vector2 coordsToScreen(int x, int y) {
        return new Vector2(INIT_X + Constants.FLOOR_SIZE * (x), INIT_Y - Constants.FLOOR_SIZE * y);
    }

    public static Vector2 screenToCoords(float x, float y) {
        return new Vector2((float)Math.floor(((x- INIT_X) / Constants.FLOOR_SIZE)), (float)Math.floor(((INIT_Y - y) / Constants.FLOOR_SIZE) +1));
    }

    public static void moveAnimal(Animal animal, float deltaTime) {
        if (animal.position != animal.desiredPosition) {
            if (Math.abs(animal.position.x - animal.desiredPosition.x) < 5) {
                animal.position.x = animal.desiredPosition.x;
            } else if (animal.position.x < animal.desiredPosition.x) {
                animal.position.x += deltaTime * animal.terminalVelocity.x;
            } else if (animal.position.x > animal.desiredPosition.x) {
                animal.position.x -= deltaTime * animal.terminalVelocity.x;
            }

            if (Math.abs(animal.position.y - animal.desiredPosition.y) < 5) {
                animal.position.y = animal.desiredPosition.y;
            }
            if (animal.position.y < animal.desiredPosition.y) {
                animal.position.y += deltaTime * animal.terminalVelocity.y;
            } else if (animal.position.y > animal.desiredPosition.y) {
                animal.position.y -= deltaTime * animal.terminalVelocity.y;
            }
            animal.rotate(deltaTime);
        }
    }
}
