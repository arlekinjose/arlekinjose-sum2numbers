variable "aws_access_key_id" {
  description = "AWS Access Key ID"
  sensitive   = true
}

variable "aws_secret_access_key" {
  description = "AWS Secret Access Key"
  sensitive   = true
}

variable "ssh_key_name" {
  description = "Name of existing AWS key pair to use (e.g. default)"
  default = "devops-test"
}

variable "region" {
  default = "sa-east-1"
}

variable "postgres_type" { default = "t3a.small" }
variable "restapi_type" { default = "t3a.small" }

provider "aws" {
  access_key = var.aws_access_key_id
  secret_key = var.aws_secret_access_key
  region = var.region
}

resource "aws_vpc" "test-env" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true
}

resource "aws_subnet" "subnet-uno" {
  # creates a subnet
  cidr_block        = cidrsubnet(aws_vpc.test-env.cidr_block, 3, 1)
  vpc_id            = aws_vpc.test-env.id
  availability_zone = "sa-east-1a"
}

resource "aws_security_group" "allow_ssh" {
  name        = "allow_ssh"
  description = "Allow SSH inbound traffic"
  vpc_id      = aws_vpc.test-env.id

  ingress {
    description = "SSH from VPC"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "allow_ssh"
  }
}

resource "aws_security_group" "allow_postgres" {
  name        = "allow_postgres"
  description = "Allow postgres  inbound traffic"
  vpc_id      = aws_vpc.test-env.id

  ingress {
    description = "postgres from VPC"
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = [aws_vpc.test-env.cidr_block]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "allow_postgres"
  }
}

resource "aws_security_group" "allow_restapi" {
  name        = "allow_restapi"
  description = "Allow restapi inbound traffic"
  vpc_id      = aws_vpc.test-env.id

  ingress {
    description = "restapi from VPC"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "allow_restapi"
  }
}

resource "aws_spot_instance_request" "postgres" {
  ami                    = "ami-054a31f1b3bf90920"
  instance_type          = var.postgres_type
  wait_for_fulfillment   = "true"
  key_name               = var.ssh_key_name
  security_groups = [aws_security_group.allow_ssh.id,aws_security_group.allow_postgres.id]
  subnet_id = aws_subnet.subnet-uno.id
}

resource "aws_spot_instance_request" "restapi" {
  ami                    = "ami-054a31f1b3bf90920"
  instance_type          = var.restapi_type
  wait_for_fulfillment   = "true"
  key_name               = var.ssh_key_name
  security_groups = [aws_security_group.allow_ssh.id,aws_security_group.allow_restapi.id]
  subnet_id = aws_subnet.subnet-uno.id
}

resource "aws_eip" "ip-postgres" {
  instance = aws_spot_instance_request.postgres.spot_instance_id
  vpc      = true
}

resource "aws_eip" "ip-restapi" {
  instance = aws_spot_instance_request.restapi.spot_instance_id
  vpc      = true
}

resource "aws_internet_gateway" "test-env-gw" {
  vpc_id = aws_vpc.test-env.id
}

resource "aws_route_table" "route-table-test-env" {
  vpc_id = aws_vpc.test-env.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.test-env-gw.id
  }
}

resource "aws_route_table_association" "subnet-association" {
  subnet_id      = aws_subnet.subnet-uno.id
  route_table_id = aws_route_table.route-table-test-env.id
}

output "restapi-public-ip" {
  value = aws_eip.ip-restapi.public_ip
}

output "postgres-public-ip" {
  value = aws_eip.ip-postgres.public_ip
}

output "postgres-private-ip" {
  value = aws_spot_instance_request.postgres.private_ip
}
