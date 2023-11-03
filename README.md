# parquimetro
Tech Challenge - Fase 3

Documentação: https://documenter.getpostman.com/view/7520874/2s9YXe84q4

Tecnologias e Ferramentas utilizadas:
- Springboot
- Java 17
- Lombok
- Bean Validator
- JPA
- PostgreSQL
- IDE IntelliJ

Funcionamento: 

  Cada nova entrada cria um registro no banco de dados com os seguintes campos: id, placa, entrada, saidaAte. 
  Ao criar a entrada é passado o número de tickets, sendo que cada ticket permite permanência por 30 minutos na vaga.
  Para verificar o status de um veículo a partir de sua placa, é verificado:
    1- se o veículo está registrado;
    2- se o horário saidaAte é maior ou igual ao horário atual;
  É possível adicionar mais tempo de permanência na vaga, adicionando mais tickets ao registro, sendo que cada ticket dá direito a mais 30 minutos de permanência.

