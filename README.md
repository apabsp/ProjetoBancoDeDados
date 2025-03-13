# ProjetoBancoDeDados


Esse Sistema de Biblioteca foi desenvolvido para a cadeira de Banco de Dados da CESAR School, onde projetamos um Banco sem utilização de ORM. Ele consiste em múltiplos domínios que lidam com diferentes funcionalidades, garantindo uma administração organizada de livros, interações com usuários e agendamentos.

Domínios

1. Domínio de Gerenciamento de Livros (Principal)

Este é o domínio central responsável por:

Registrar e catalogar livros.

Gerenciar empréstimos e devoluções de livros.

Acompanhar a disponibilidade e o histórico de empréstimos.

2. Domínio de Contato Telefônico (Suporte)

Este domínio gerencia a comunicação e o suporte, incluindo:

Armazenamento e gerenciamento de informações de contato para suporte ao usuário.

Atendimento a dúvidas e reclamações.

3. Domínio de Calendário (Genérico)

Este domínio é responsável pelo agendamento e gerenciamento de tempo dentro da biblioteca, incluindo:

Controle de prazos para devolução de livros.

Agendamento de salas de estudo e eventos especiais.

Manutenção dos horários de funcionamento e feriados.

Arquitetura

O sistema segue uma abordagem modular, onde cada domínio é projetado para funcionar de forma independente, mas pode interagir perfeitamente com os outros para proporcionar uma experiência integrada de gerenciamento da biblioteca.
