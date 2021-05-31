variable "amis" { type = "map" }
variable "region" {}
variable "azs" { type = "map" }
variable "instance_count" {}
variable "instance_name" {}
variable "key_name" {}
variable "vpc_security_group_ids" { type = "list" }
variable "subnets" { type = "list" }
variable "instance_type" {}
variable "volume_size" {}

resource "aws_instance" "instance" {
  instance_type = var.instance_type
  ami = lookup(var.amis, var.region)

  count = var.instance_count
  key_name = var.key_name
  vpc_security_group_ids = [var.vpc_security_group_ids]
  associate_public_ip_address = true
  disable_api_termination = true
  source_dest_check = false

  availability_zone = element(split(",", lookup(var.azs, var.region)), count.index % length(split(",", lookup(var.azs, var.region))))
  subnet_id = var.subnets[count.index % length(split(",", lookup(var.azs, var.region)))]

  tags {
          Name = var.instance_name-count.index
      }

  root_block_device {
    volume_size = var.volume_size
    delete_on_termination = true
  }

}

output "instances" {
  value = [aws_instance.instance.*.id]
}

output "public_ips" {
  value = [aws_instance.instance.*.public_ip]
}

output "private_ips" {
  value = [aws_instance.instance.*.private_ip]
}
