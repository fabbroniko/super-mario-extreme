package com.fabbroniko.environment;

import com.fabbroniko.gameobjects.AbstractGameObject;
import com.fabbroniko.scene.GameScene;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineSegment;

@Log4j2
public class CollisionManager {

    // This movement offset is in place to prevent objects from going through map/objects
    // due to fps spikes that would make the computed offset being too big,
    private static final int MAX_MOVEMENT_OFFSET = 90;

    final private TileMap tileMap;
    final private GameScene gameScene;

    public CollisionManager(final TileMap tileMap, final GameScene gameScene) {
        this.tileMap = tileMap;
        this.gameScene = gameScene;
    }

    public CollisionResult detectCollisions(final AbstractGameObject objectToMove, final Vector2D offset) {
        if(offset.getX() > MAX_MOVEMENT_OFFSET)
            offset.setX(MAX_MOVEMENT_OFFSET);

        if(offset.getY() > MAX_MOVEMENT_OFFSET)
            offset.setY(MAX_MOVEMENT_OFFSET);

        // Check collision with map - if the new computed offset is 0 then skip the check for game objects.
        CollisionResult collisionResult = checkForMapCollision(objectToMove, offset);
        if(collisionResult.getOffset().getX() == 0 && collisionResult.getOffset().getY() == 0)
            return collisionResult;

        for(final AbstractGameObject obj : gameScene.getGameObjects()) {
            if(!objectToMove.equals(obj))
                collisionResult = checkForGameObjectsCollision(objectToMove, obj, collisionResult);
        }

        return collisionResult;
    }

    public CollisionResult checkForGameObjectsCollision(final AbstractGameObject objectToMove, final AbstractGameObject gameObjectToCheck, final CollisionResult postMapProcessingCollisionResult) {
        final CollisionResult collisionResult = computeCollision(objectToMove.getPosition(), objectToMove.getSpriteDimension(), gameObjectToCheck.getPosition(), gameObjectToCheck.getSpriteDimension(), postMapProcessingCollisionResult.getOffset(), objectToMove);

        if(collisionResult.isHasCollided()) {
            gameObjectToCheck.collisionHandler(new CollisionResult(new Vector2D(), false, objectToMove, CollisionDirection.invert(collisionResult.getCollisionDirection())));
        }

        return collisionResult;
    }

    public CollisionResult checkForMapCollision(final AbstractGameObject objectToMove, final Vector2D offset) {
        final Vector2D dimension = objectToMove.getSpriteDimension();
        final Vector2D currentPosition = objectToMove.getPosition();
        final Vector2D adjustedOffset = offset.clone();

        if(offset.getY() > 0) {
            final TileMap.TileInfo bottomLeftCorner = tileMap.getTileType(
                currentPosition.getRoundedX(),
                (int)Math.round(currentPosition.getY() + dimension.getY() + offset.getY())
            );
            final TileMap.TileInfo bottomRightCorner = tileMap.getTileType(
                currentPosition.getRoundedX() + dimension.getRoundedX(),
                (int)Math.round(currentPosition.getY() + dimension.getY() + offset.getY())
            );

            if(TileType.BLOCKING.equals(bottomLeftCorner.getTileType())) {
                final CollisionResult collisionResult = computeCollision(currentPosition, dimension, bottomLeftCorner.getOrigin(), bottomLeftCorner.getDimension(), offset, objectToMove);
            }
        } else if (offset.getY() < 0) {

        }




        // Checking collision for the top left corner of the game object's hit box
        final TileMap.TileInfo topLeftCorner = tileMap.getTileType(
                partialFinalDestination.getRoundedX(),
                partialFinalDestination.getRoundedY());
        if(topLeftCorner != null && topLeftCorner.getTileType().equals(TileType.BLOCKING)) {
            final CollisionResult collisionResult = computeCollision(currentPosition, dimension, topLeftCorner.getOrigin(), topLeftCorner.getDimension(), partialNewOffset, null);
            collisionDirection = collisionResult.getCollisionDirection();
            partialNewOffset = collisionResult.getOffset();
        }

        // Checking collision for the top right corner of the game object's hit box
        partialFinalDestination = currentPosition.clone().sum(partialNewOffset);
        final TileMap.TileInfo topRightCorner = tileMap.getTileType(
                partialFinalDestination.getRoundedX() + dimension.getRoundedX() - 1,
                partialFinalDestination.getRoundedY());
        if(topRightCorner != null && topRightCorner.getTileType().equals(TileType.BLOCKING)) {
            final CollisionResult collisionResult =  computeCollision(currentPosition, dimension, topRightCorner.getOrigin(), topRightCorner.getDimension(), partialNewOffset, null);
            partialNewOffset = collisionResult.getOffset();

            if(collisionDirection == null)
                collisionDirection = collisionResult.getCollisionDirection();
        }

        partialFinalDestination = currentPosition.clone().sum(partialNewOffset);
        final TileMap.TileInfo bottomLeftCorner = tileMap.getTileType(
                partialFinalDestination.getRoundedX(),
                partialFinalDestination.getRoundedY() + dimension.getRoundedY() - 1);
        if(bottomLeftCorner != null && bottomLeftCorner.getTileType().equals(TileType.BLOCKING)) {
            final CollisionResult collisionResult = computeCollision(currentPosition, dimension, bottomLeftCorner.getOrigin(), bottomLeftCorner.getDimension(), partialNewOffset, null);
            partialNewOffset = collisionResult.getOffset();

            if(collisionDirection == null)
                collisionDirection = collisionResult.getCollisionDirection();
        }

        partialFinalDestination = currentPosition.clone().sum(partialNewOffset);
        final TileMap.TileInfo bottomRightCorner = tileMap.getTileType(
                partialFinalDestination.getRoundedX() + dimension.getRoundedX() - 1,
                partialFinalDestination.getRoundedY() + dimension.getRoundedY() - 1);
        if(bottomRightCorner != null && bottomRightCorner.getTileType().equals(TileType.BLOCKING)) {
            final CollisionResult collisionResult = computeCollision(currentPosition, dimension, bottomRightCorner.getOrigin(), bottomRightCorner.getDimension(), partialNewOffset, null);
            partialNewOffset = collisionResult.getOffset();

            if(collisionDirection == null)
                collisionDirection = collisionResult.getCollisionDirection();
        }

        return new CollisionResult(partialNewOffset, collisionDirection != null, null, collisionDirection);
    }

    public CollisionResult computeCollision(final Vector2D objectToMovePosition,
                                            final Vector2D objectToMoveDimension,
                                            final Vector2D impactedObjectPosition,
                                            final Vector2D impactedObjectDimension,
                                            final Vector2D offset,
                                            final AbstractGameObject objectToMove) {

        final LineSegment impactedObjectTopSegment = new LineSegment(impactedObjectPosition.getX(),
                impactedObjectPosition.getY(),
                impactedObjectPosition.getX() + impactedObjectDimension.getRoundedX() - 1,
                impactedObjectPosition.getY());

        final LineSegment impactedObjectRightSegment = new LineSegment(impactedObjectPosition.getX() + impactedObjectDimension.getRoundedX() - 1,
                impactedObjectPosition.getY(),
                impactedObjectPosition.getX() + impactedObjectDimension.getRoundedX() - 1,
                impactedObjectPosition.getY() + impactedObjectDimension.getRoundedY() - 1);

        final LineSegment impactedObjectBottomSegment = new LineSegment(impactedObjectPosition.getX(),
                impactedObjectPosition.getY() + impactedObjectDimension.getRoundedY() - 1,
                impactedObjectPosition.getX() + impactedObjectDimension.getRoundedX() - 1,
                impactedObjectPosition.getY() + impactedObjectDimension.getRoundedY() - 1);

        final LineSegment impactedObjectLeftSegment = new LineSegment(impactedObjectPosition.getX(),
                impactedObjectPosition.getY(),
                impactedObjectPosition.getX(),
                impactedObjectPosition.getY() + impactedObjectDimension.getRoundedY() - 1);

        final LineSegment objTopLeftProjection = new LineSegment(
                objectToMovePosition.getX(),
                objectToMovePosition.getY(),
                objectToMovePosition.getX() + offset.getX(),
                objectToMovePosition.getY() + offset.getY()
        );

        final LineSegment objTopRightProjection = new LineSegment(
                objectToMovePosition.getX() + objectToMoveDimension.getRoundedX() - 1,
                objectToMovePosition.getY(),
                objectToMovePosition.getX() + objectToMoveDimension.getRoundedX() - 1 + offset.getX(),
                objectToMovePosition.getY() + offset.getY()
        );

        final LineSegment objBottomLeftProjection = new LineSegment(
                objectToMovePosition.getX(),
                objectToMovePosition.getY() + objectToMoveDimension.getRoundedY() - 1,
                objectToMovePosition.getX() + offset.getX(),
                objectToMovePosition.getY() + objectToMoveDimension.getRoundedY() - 1 + offset.getY()
        );

        final LineSegment objBottomRightProjection = new LineSegment(
                objectToMovePosition.getX() + objectToMoveDimension.getRoundedX() - 1,
                objectToMovePosition.getY() + objectToMoveDimension.getRoundedY() - 1,
                objectToMovePosition.getX() + objectToMoveDimension.getRoundedX() - 1 + offset.getX(),
                objectToMovePosition.getY() + objectToMoveDimension.getRoundedY() - 1 + offset.getY()
        );

        if(offset.getX() > 0) {
            Coordinate intersection1 = objTopRightProjection.intersection(impactedObjectLeftSegment);
            Coordinate intersection2 = objBottomRightProjection.intersection(impactedObjectLeftSegment);

            if(intersection1 != null || intersection2 != null) {
                double tempX = intersection1 != null ? intersection1.getX() : intersection2.getX();
                double newOffsetX = tempX - objectToMovePosition.getX() - objectToMoveDimension.getRoundedX();
                return new CollisionResult(new Vector2D(newOffsetX, offset.getY()), true, objectToMove, CollisionDirection.RIGHT_COLLISION);
            }
        } else if (offset.getX() < 0) {
            Coordinate intersection1 = objTopLeftProjection.intersection(impactedObjectRightSegment);
            Coordinate intersection2 = objBottomLeftProjection.intersection(impactedObjectRightSegment);

            if(intersection1 != null || intersection2 != null) {
                double tempX = intersection1 != null ? intersection1.getX() : intersection2.getX();
                double newOffsetX = tempX - objectToMovePosition.getX() + 1;
                return new CollisionResult(new Vector2D(newOffsetX, offset.getY()), true, objectToMove, CollisionDirection.LEFT_COLLISION);
            }
        }

        if(offset.getY() > 0) {
            Coordinate intersection1 = objBottomLeftProjection.intersection(impactedObjectTopSegment);
            Coordinate intersection2 = objBottomRightProjection.intersection(impactedObjectTopSegment);

            if(intersection1 != null || intersection2 != null) {
                double tempY = intersection1 != null ? intersection1.getY() : intersection2.getY();
                double newOffsetY = tempY - objectToMovePosition.getY() - objectToMoveDimension.getRoundedY();
                return new CollisionResult(new Vector2D(offset.getX(), newOffsetY), true, objectToMove, CollisionDirection.BOTTOM_COLLISION);
            }
        } else if (offset.getY() < 0) {
            Coordinate intersection1 = objTopLeftProjection.intersection(impactedObjectBottomSegment);
            Coordinate intersection2 = objTopRightProjection.intersection(impactedObjectBottomSegment);

            if(intersection1 != null || intersection2 != null) {
                double tempY = intersection1 != null ? intersection1.getY() : intersection2.getY();
                double newOffsetY = tempY - objectToMovePosition.getY() + 1;
                return new CollisionResult(new Vector2D(offset.getX(), newOffsetY), true, objectToMove, CollisionDirection.TOP_COLLISION);
            }
        }

        // The algorithm determined a collision was detected but it failed to compute the new offset and direction of the collision.
        return new CollisionResult(offset, false, null, null);
    }

    @Getter
    @AllArgsConstructor
    public static class CollisionResult {

        final private Vector2D offset;

        final private boolean hasCollided;

        final private AbstractGameObject collidedWith;

        final private CollisionDirection collisionDirection;
    }
}
