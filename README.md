# zew-donations

## GitHub Actions for Deployment

This details steps for configuring GitHub actions to automatically deploy application changes to ECS Faragte on AWS for commits.

## Pre-requisites

1. The donations service must be deployed to AWS. Refer to `zew-infra/donations_service_stack` for instructions to do this. You will not be able to create a workflow directly in a non-main branch due to a GitHub [gap](https://github.community/t/workflow-files-only-picked-up-from-master/16129/42). Create a dummy workflow in main directly. Once this is done, you can create the actual workflow in other branches.

2. Download the ECS task definition. An existing task definition can be downloaded to a JSON file with the following command. Account IDs can be removed from the file by removing the taskDefinitionArn attribute, and updating the executionRoleArn and taskRoleArn attribute values to contain role names instead of role ARNs. To find the task definition family name, go to ECS console and `Task Definitions`. Note down name of definition starting with `ZewDonationsStack...`. Substitire in command below and follow instructions for removing the taskDefinitionArn attribute, and updating the executionRoleArn and taskRoleArn attributes.

```bash
aws ecs describe-task-definition \
   --task-definition <task-definition-family> \
   --query taskDefinition > task-definition.json
```

Save this file in a `.aws` folder in the root of the repository. Commit this file to the repository.

3. GitHub needs AWS credentials to deploy. In order to do this, create an IAM user and set up access key for the user. Note down the keys. For donations service, the user `zew-donations-github-actions` was created from the console. Next attach IAM permissions to this. Refer to the [AWS documentation](https://github.com/aws-actions/amazon-ecs-deploy-task-definition#permissions) for actual policy. Substitite placeholders within `< >` from the task definition file you got in step 2.

4. You need to then store the access key and secret access key in GitHub Action Secrets. Follow [GitHub documentation](https://docs.github.com/en/actions/security-guides/encrypted-secrets) for this.

## GitHub Actions Workflow

1. Go to GitHub actions on the repository and create a new workflow. You will find a `Deploy to Amazon ECS` workflow. Click on `Setup this workflow`.

2. You need to specify following environment variables which you can get from task definition.

```bash
  AWS_REGION: MY_AWS_REGION                   # set this to your preferred AWS region, e.g. us-west-1
  ECR_REPOSITORY: MY_ECR_REPOSITORY           # set this to your Amazon ECR repository name
  ECS_SERVICE: MY_ECS_SERVICE                 # set this to your Amazon ECS service name
  ECS_CLUSTER: MY_ECS_CLUSTER                 # set this to your Amazon ECS cluster name
  ECS_TASK_DEFINITION: MY_ECS_TASK_DEFINITION # set this to the path to your Amazon ECS task definition
                                               # file, e.g. .aws/task-definition.json
  CONTAINER_NAME: MY_CONTAINER_NAME           # set this to the name of the container in the
                                               # containerDefinitions section of your task definition
```

3. Once variables in file are updated, click on start a commit. This should complete if all values were specified.

## Things to watch out for

1. If you re-deploy the cluster, the workflow file as well as task definition must be updated to reflect new value of cluster and related details like task definition. IAM role for user must also be updated. Else workflow will fail.

2. Current user for GitHub actions - `zew-donations-github-actions` - can only deploy to the donations cluster.


This document provides detailed descriptions of the controllers and services in the application. Each controller and service is explained with their respective functionalities and interactions. This serves as a reference to understand the structure and responsibilities within the application.

---

## Controllers

### MissionController

The `MissionController` handles requests related to missions.

#### Methods:

- **getTotalMissionDonors**: Fetches the total number of donors for a given mission.
  - **Endpoint**: `/mission/total-donors/{id}`
  - **HTTP Method**: GET
  - **Parameters**: `id` (Path variable) - The ID of the mission.
  - **Returns**: An integer representing the number of unique donors for the mission.

### RevenueController

The `RevenueController` handles operations related to revenues.

#### Methods:

- **findByOwnerId**: Fetches all revenues for a given owner.
  - **Endpoint**: `/revenues/findByOwnerId`
  - **HTTP Method**: GET
  - **Parameters**: `ownerId` (Request parameter) - The ID of the owner.
  - **Returns**: A list of `Revenue` objects.

- **getFinancialsByOwnerId**: Retrieves financial data for a given owner.
  - **Endpoint**: `/revenues/{ownerId}`
  - **HTTP Method**: GET
  - **Parameters**: 
    - `ownerId` (Path variable) - The ID of the owner.
    - `currency` (Request parameter, default: "USD") - The currency in which to return the financial data.
  - **Returns**: A `ResponseEntity` containing a map with financial details.

- **create**: Creates a new revenue entry.
  - **Endpoint**: `/revenues`
  - **HTTP Method**: POST
  - **Parameters**: `revenue` (Request body) - The `Revenue` object to be created.
  - **Returns**: An `EntityResponse` representing the created revenue.

- **getTotalAmountByOwnerID**: Fetches total amount data by owner ID.
  - **Endpoint**: `/revenues/ownerId/{ownerId}`
  - **HTTP Method**: GET
  - **Parameters**: `ownerId` (Request parameter) - The ID of the owner.
  - **Returns**: A list of `Revenue` objects.

### WalletController

The `WalletController` manages wallet-related operations.

#### Methods:

- **create**: Creates a new wallet.
  - **Endpoint**: `/wallet`
  - **HTTP Method**: POST
  - **Parameters**: `wallet` (Request body) - The `Wallet` object to be created.
  - **Returns**: An `EntityResponse` representing the created wallet.

- **getById**: Fetches a wallet by its ID.
  - **Endpoint**: `/wallet/{id}`
  - **HTTP Method**: GET
  - **Parameters**: `id` (Path variable) - The ID of the wallet.
  - **Returns**: A `WalletResponse` representing the wallet.

- **updateAmounts**: Updates the amounts in a wallet.
  - **Endpoint**: `/wallet/{id}`
  - **HTTP Method**: PUT
  - **Parameters**: 
    - `id` (Path variable) - The ID of the wallet.
    - `request` (Request body) - The `WalletUpdateRequest` object containing update details.
  - **Returns**: An `EntityResponse` representing the updated wallet.

## Services

### DonationService

The `DonationService` interface and its implementation manage donation-related operations.

#### Methods:

- **saveDonation**: Saves a donation to the database.
  - **Parameters**: `donationDto` - The `DonationDto` object containing donation details.

### RevenueService

The `RevenueService` interface and its implementation handle revenue-related operations.

#### Methods:

- **findById**: Retrieves a revenue by its ID.
  - **Parameters**: `id` - The ID of the revenue.
  - **Returns**: A `Revenue` object.

- **create**: Creates a new revenue entry.
  - **Parameters**: `revenue` - The `Revenue` object to be created.
  - **Returns**: The created `Revenue` object.

- **getAllRevenuesByOwnerId**: Fetches all revenues for a given owner.
  - **Parameters**: `ownerId` - The ID of the owner.
  - **Returns**: A list of `Revenue` objects.

- **getFinancialsByOwnerId**: Retrieves financial data for a given owner.
  - **Parameters**: 
    - `ownerId` - The ID of the owner.
    - `currency` - The currency in which to return the financial data.
  - **Returns**: A map containing financial details.

- **getAllTotalAmountByOwnerId**: Fetches total amount data by owner ID.
  - **Parameters**: `ownerId` - The ID of the owner.
  - **Returns**: A list of `Revenue` objects.

### WalletService

The `WalletService` interface and its implementation manage wallet-related operations.

#### Methods:

- **findById**: Retrieves a wallet by its ID.
  - **Parameters**: `id` - The ID of the wallet.
  - **Returns**: A `Wallet` object.

- **create**: Creates a new wallet.
  - **Parameters**: `wallet` - The `Wallet` object to be created.
  - **Returns**: The created `Wallet` object.

- **updateAmounts**: Updates the amounts in a wallet.
  - **Parameters**: 
    - `walletId` - The ID of the wallet.
    - `request` - The `WalletUpdateRequest` object containing update details.
  - **Returns**: The updated `Wallet` object.

- **findByOwnerIdAndType**: Fetches a wallet by owner ID and type.
  - **Parameters**: 
    - `ownerId` - The ID of the owner.
    - `walletType` - The type of the wallet.
  - **Returns**: A `Wallet` object.

- **update**: Updates an existing wallet.
  - **Parameters**: `wallet` - The `Wallet` object to be updated.

- **getCountByMissionIdGroupByOwnerId**: Retrieves the count of donors for a mission grouped by owner ID.
  - **Parameters**: `missionId` - The ID of the mission.
  - **Returns**: An integer representing the count of donors.

---

## Exception Handling

### WalletAlreadyExistsException

Exception thrown when trying to create a wallet that already exists.

### WalletNotFoundException

Exception thrown when a wallet is not found.

### RevenueAlreadyExistsException

Exception thrown when trying to create a revenue entry that already exists.

---

## Converters

### EntityConverter

Handles conversion between entities and their response representations.

### WalletConverter

Handles conversion between `Wallet` entities and their response representations.

---

This documentation provides a comprehensive overview of the controllers and services within the application, including their methods and interactions. Use this as a reference to understand the flow and operations within the system.