###Описание проблемы
Требуется обеспечить обработку исключительных ситуаций, возникших при работе _Transition_ Action'ов таким образом, чтобы 
состояние StateMachine в случае возникновения ошибки в процессе перехода оставалось неизменным. Предполагается, что для 
достижения этой цели можо воспользоваться 2 вариантами: 
- [ErrorAction](https://docs.spring.io/spring-statemachine/docs/current/api/org/springframework/statemachine/config/configurers/AbstractTransitionConfigurer.html#addAction-org.springframework.statemachine.action.Action-org.springframework.statemachine.action.Action-) 
- [Guard](https://docs.spring.io/spring-statemachine/docs/current/api/index.html?org/springframework/statemachine/guard/Guard.html)
Необходимо проверить возможность использования указанных вариантов, перечислить различия и выбрать наиболее подходящий
###Подход к решению
Для проверки было решено написать набор тестов, включающий:
- эталонное поведение, без обработки исключительной ситуации, возникшей в работе transition action'а 
- обработка при помощи error action'а
- обработка при помощи guard'а
###Результаты
В результате тестов было установлено следующее: 
- в случае возникновении ошибки в ходе работы transition action'а state machine в любом случае не меняет своего состояния
- обработка ошибки при помощи error action'а предоставляет механизм доп обработки исключения, не влияя на изменение состояния StateMachine
- Guard срабатывает раньше Transtion Action'ов, поэтому наивная реализация не пригодна для обработки исключений, возникших в transition action'ах
###Заключение
- использование error action'ов удовлетворяет сформулированным требованиям для обработки transtion action'ов и является рекомендованным способомв соответствии с [официальной документацией](https://docs.spring.io/spring-statemachine/docs/current/reference/#statemachine-config-transition-actions-errorhandling)
- использование guard'ов для обрабоки исключений невозможно в тривиальном виде
###Примечание
Для обоработки ошибок самой state machine можно использовать два чуть более сложных подхода:
- реализация интерфейса [StateMachineInterceptor](https://docs.spring.io/spring-statemachine/docs/current/api/index.html?org/springframework/statemachine/guard/Guard.html), особенностью которой является возможность "проглатывать" ошибки, чтобы заглушить ее дальнейшую обработку
- наследование класса [StateMachineListenerAdapter](https://docs.spring.io/spring-statemachine/docs/current/api/org/springframework/statemachine/listener/StateMachineListenerAdapter.html), использующее механизмы Spring, при этом налагая на конфигурирование StateMachine соответствующие требования 
При этом, тривиальная обработка ошибок, возникших при работе Transition Action'ов, указанными выше способоми невозможна