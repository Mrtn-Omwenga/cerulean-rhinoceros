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
