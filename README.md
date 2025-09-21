
## Como Testar

- Upload do arquivo:
```
curl -X POST -F "file=@/path/to/file/CNAB.txt" http://localhost:8080/cnab/upload
``` 
- Lista das operações importadas com totalizador por nome da loja:
```
curl http://localhost:8080/transacoes
```
