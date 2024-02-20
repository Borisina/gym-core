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

## Delete gym-itest app
gym-itest-rm:
	docker rm -f gym-itest-container
	docker rmi gym-itest-image

## Build and run gym-itest app
gym-itest-up:
	docker build -t gym-itest-image -f dockerfile-itest .
	docker run -dp 8085:8085\
		--network gym_gym-network-itest --network-alias gym-itest\
	    --env-file .env\
	    --name gym-itest-container\
	     gym-itest-image

## Enter to database console
shell-db:
	docker compose exec postgres psql -U ${POSTGRES_USER} -d ${POSTGRES_DB}