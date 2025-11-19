import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

def Message processData(Message message) {
    
    // Obter o corpo da mensagem (JSON do SAP)
    def body = message.getBody(String.class)
    def jsonSlurper = new JsonSlurper()
    def sapResponse = jsonSlurper.parseText(body)
    
    // Processar os dados dos fornecedores
    def fornecedores = sapResponse.collect { item ->
        [
            codigoFornecedor: item.lifnr?.trim() ?: "",
            nome: item.name1?.trim() ?: "",
            cidade: item.ort01?.trim() ?: "",
            regiao: item.regio?.trim() ?: "",
            pais: item.land1?.trim() ?: ""
        ]
    }
    
    // Criar resposta formatada
    def output = [
        status: "success",
        timestamp: new Date().format("yyyy-MM-dd'T'HH:mm:ss'Z'"),
        totalRegistros: fornecedores.size(),
        fornecedores: fornecedores
    ]
    
    message.setBody(JsonOutput.toJson(output))
    message.setHeader("Content-Type", "application/json")
    
    return message
}
