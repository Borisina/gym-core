include .env

## Delete gym app
gym-rm:
	docker rm -f gym-container
	docker rmi gym-image

## Build and run gym app
gym-up:
	docker build -t gym-image .
	docker run -dp 8080:8080\
	    --network gym_gym-network --network-alias gym\
	    --env-file .env\
	    --name gym-container\
	    --mount type=volume,source=logsvolume,target=/app/logs\
	    gym-image

## Enter to database console
shell-db:
	docker compose exec postgres psql -U ${POSTGRES_USER} -d ${POSTGRES_DB}